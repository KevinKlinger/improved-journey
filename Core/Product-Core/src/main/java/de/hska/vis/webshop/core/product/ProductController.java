package de.hska.vis.webshop.core.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private IProductDAO dao = DaoFactory.getProductDao();

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<Void> saveProduct(Product product) {
        HttpStatus code;
        if (dao.saveObject(product)) {
            code = HttpStatus.OK;
        } else {
            code = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(code);
    }

    @RequestMapping(value = "/product/{stringId}", method = RequestMethod.GET)
    public ResponseEntity<IProduct> getProductById(@PathVariable String stringId) {
        HttpStatus code = HttpStatus.OK;
        IProduct product;
        try {
            Integer parsedId = Integer.parseInt(stringId);
            product = dao.getObjectById(parsedId);
            if (product == null) {
                code = HttpStatus.NOT_FOUND;
            }
        } catch (NumberFormatException ex) {
            // invalid number supplied; returning null and HTTP/400
            product = null;
            code = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<IProduct>(product, code);
    }

    @RequestMapping(value = "/product/{stringId}", method = RequestMethod.PUT)
    public ResponseEntity<IProduct> updateProduct(@PathVariable String stringId, Product product) {
        HttpStatus code = HttpStatus.OK;
        try {
            Integer parsedId = Integer.parseInt(stringId);
            IProduct savedProduct = dao.getObjectById(parsedId);
            if (product == null) {
                code = HttpStatus.NOT_FOUND;
            } else {
                savedProduct.setName(product.getName());
                savedProduct.setPrice(product.getPrice());
                savedProduct.setCategory(product.getCategory());
                savedProduct.setDetails(product.getDetails());
                if (dao.updateObject(savedProduct)) {
                    // debugging code if something goes wrong while saving
                    code = HttpStatus.I_AM_A_TEAPOT;
                }
            }
        } catch (NumberFormatException ex) {
            // invalid number supplied; returning null and HTTP/400
            product = null;
            code = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<IProduct>(product, code);
    }

    @RequestMapping(value = "/product/{stringId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProductById(@PathVariable String stringId) {
        HttpStatus code;
        try {
            Integer parsedId = Integer.parseInt(stringId);
            if (dao.deleteById(parsedId)) {
                code = HttpStatus.OK;
            } else {
                code = HttpStatus.NOT_FOUND;
            }
        } catch (NumberFormatException ex) {
            // invalid number supplied; returning null and HTTP/400
            code = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(code);
    }
}