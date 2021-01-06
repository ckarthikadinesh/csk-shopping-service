package com.csk.shopping.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csk.shopping.constants.ResponseCode;
import com.csk.shopping.constants.WebConstants;
import com.csk.shopping.exception.AddressException;
import com.csk.shopping.exception.CartException;
import com.csk.shopping.exception.PlaceOrderException;
import com.csk.shopping.exception.ProductException;
import com.csk.shopping.exception.UserException;
import com.csk.shopping.model.Address;
import com.csk.shopping.model.Cart;
import com.csk.shopping.model.Orders;
import com.csk.shopping.model.Product;
import com.csk.shopping.model.User;
import com.csk.shopping.repository.AddressRepository;
import com.csk.shopping.repository.CartRepository;
import com.csk.shopping.repository.OrderRepository;
import com.csk.shopping.repository.ProductRepository;
import com.csk.shopping.repository.UserRepository;
import com.csk.shopping.dto.CartResponse;
import com.csk.shopping.dto.ProductResponse;
import com.csk.shopping.dto.Response;
import com.csk.shopping.dto.ServerResponse;
import com.csk.shopping.dto.UserResponse;
import com.csk.shopping.util.Validator;

@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("/user")
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class.getName());

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AddressRepository addrRepo;

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private OrderRepository ordRepo;

	@PostMapping("/addAddress")
	public ResponseEntity<UserResponse> addAddress(@RequestBody Address address, Authentication auth) {
		UserResponse resp = new UserResponse();
		if (Validator.isAddressEmpty(address)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
			return new ResponseEntity<UserResponse>(resp, HttpStatus.NOT_ACCEPTABLE);
		} else {
			try {
				User user = userRepo.findByUsername(auth.getName())
						.orElseThrow(() -> new UsernameNotFoundException(auth.getName()));
				Address userAddress = addrRepo.findByUser(user);
				if (userAddress != null) {
					userAddress.setAddress(address.getAddress());
					userAddress.setCity(address.getCity());
					userAddress.setCountry(address.getCountry());
					userAddress.setPhonenumber(address.getPhonenumber());
					userAddress.setState(address.getState());
					userAddress.setZipcode(address.getZipcode());
					addrRepo.save(userAddress);
				} else {
					user.setAddress(address);
					address.setUser(user);
					addrRepo.save(address);
				}
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.CUST_ADR_ADD);
			} catch (Exception e) {
				throw new AddressException("Unable to add address, please try again");
			}
		}
		return new ResponseEntity<UserResponse>(resp, HttpStatus.OK);
	}

	@GetMapping("/getAddress")
	public ResponseEntity<Response> getAddress(Authentication auth) {
		Response resp = new Response();
		try {
			User user = userRepo.findByUsername(auth.getName()).orElseThrow(
					() -> new UserException("User with username " + auth.getName() + " doesn't exists"));
			Address adr = addrRepo.findByUser(user);

			HashMap<String, String> map = new HashMap<>();
			map.put(WebConstants.ADR_NAME, adr.getAddress());
			map.put(WebConstants.ADR_CITY, adr.getCity());
			map.put(WebConstants.ADR_STATE, adr.getState());
			map.put(WebConstants.ADR_COUNTRY, adr.getCountry());
			map.put(WebConstants.ADR_ZP, String.valueOf(adr.getZipcode()));
			map.put(WebConstants.PHONE, adr.getPhonenumber());

			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.CUST_ADR_ADD);
			resp.setMap(map);
		} catch (Exception e) {
			throw new AddressException("Unable to retrieve address, please try again");
		}
		return new ResponseEntity<Response>(resp, HttpStatus.OK);
	}

	@GetMapping("/getProducts")
	public ResponseEntity<ProductResponse> getProducts(Authentication auth) throws IOException {
		ProductResponse resp = new ProductResponse();
		try {
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.LIST_SUCCESS_MESSAGE);
			resp.setOblist(prodRepo.findAll());
		} catch (Exception e) {
			throw new ProductException("Unable to retrieve products, please try again");
		}
		return new ResponseEntity<ProductResponse>(resp, HttpStatus.OK);
	}

	@GetMapping("/addToCart")
	public ResponseEntity<ServerResponse> addToCart(@RequestParam(WebConstants.PROD_ID) String productId,
			Authentication auth) throws IOException {

		ServerResponse resp = new ServerResponse();
		try {
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserException(auth.getName()));
			Product cartItem = prodRepo.findByProductid(Integer.parseInt(productId));

			Cart cart = new Cart();
			cart.setEmail(loggedUser.getEmail());
			cart.setQuantity(1);
			cart.setPrice(cartItem.getPrice());
			cart.setProductId(Integer.parseInt(productId));
			cart.setProductname(cartItem.getProductname());
			Date date = new Date();
			cart.setDateAdded(date);

			cartRepo.save(cart);

			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.CART_UPD_MESSAGE_CODE);
		} catch (Exception e) {
			throw new CartException("Unable to add product to cart, please try again");
		}
		return new ResponseEntity<ServerResponse>(resp, HttpStatus.OK);
	}

	@GetMapping("/viewCart")
	public ResponseEntity<CartResponse> viewCart(Authentication auth) throws IOException {
		logger.info("Inside View cart request method");
		CartResponse resp = new CartResponse();
		try {
			logger.info("Inside View cart request method 2");
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserException(auth.getName()));
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.VW_CART_MESSAGE);
			resp.setOblist(cartRepo.findByEmail(loggedUser.getEmail()));
		} catch (Exception e) {
			throw new CartException("Unable to retrieve cart items, please try again");
		}

		return new ResponseEntity<CartResponse>(resp, HttpStatus.OK);
	}

	@PutMapping("/updateCart")
	public ResponseEntity<CartResponse> updateCart(@RequestBody HashMap<String, String> cart, Authentication auth)
			throws IOException {

		CartResponse resp = new CartResponse();
		try {
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserException(auth.getName()));
			Cart selCart = cartRepo.findByCartIdAndEmail(Integer.parseInt(cart.get("id")), loggedUser.getEmail());
			selCart.setQuantity(Integer.parseInt(cart.get("quantity")));
			cartRepo.save(selCart);
			List<Cart> cartlist = cartRepo.findByEmail(loggedUser.getEmail());
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.UPD_CART_MESSAGE);
			resp.setOblist(cartlist);
		} catch (Exception e) {
			throw new CartException("Unable to update cart items, please try again");
		}

		return new ResponseEntity<CartResponse>(resp, HttpStatus.OK);
	}

	@DeleteMapping("/deleteCart")
	public ResponseEntity<CartResponse> delCart(@RequestParam(name = WebConstants.CART_ID) String cartid,
			Authentication auth) throws IOException {

		CartResponse resp = new CartResponse();
		try {
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserException(auth.getName()));
			cartRepo.deleteByCartIdAndEmail(Integer.parseInt(cartid), loggedUser.getEmail());
			List<Cart> cartlist = cartRepo.findByEmail(loggedUser.getEmail());
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.DEL_CART_SUCCESS_MESSAGE);
			resp.setOblist(cartlist);
		} catch (Exception e) {
			throw new CartException("Unable to delete cart items, please try again");
		}
		return new ResponseEntity<CartResponse>(resp, HttpStatus.OK);
	}

	@GetMapping("/placeOrder")
	public ResponseEntity<ServerResponse> placeOrder(Authentication auth) throws IOException {

		ServerResponse resp = new ServerResponse();
		try {
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserException(auth.getName()));
			Orders po = new Orders();
			po.setEmail(loggedUser.getEmail());
			Date date = new Date();
			po.setOrderDate(date);
			po.setOrderStatus(ResponseCode.ORD_STATUS_CODE);
			double total = 0;
			List<Cart> cartlist = cartRepo.findAllByEmail(loggedUser.getEmail());
			for (Cart cart : cartlist) {
				total = +(cart.getQuantity() * cart.getPrice());
			}
			po.setTotalCost(total);
			Orders res = ordRepo.save(po);
			cartlist.forEach(cart -> {
				cart.setOrderId(res.getOrderId());
				cartRepo.save(cart);

			});
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.ORD_SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new PlaceOrderException("Unable to place order, please try again later");
		}
		return new ResponseEntity<ServerResponse>(resp, HttpStatus.OK);
	}
}
