package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.component.NullAwareBeanUtilsBean;
import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.domain.image.ProductImage;
import ch.fhnw.palletpals.data.repository.ProductImageRepository;
import ch.fhnw.palletpals.data.repository.ProductRepository;
import ch.fhnw.palletpals.data.repository.ShoppingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class ShoppingSessionService {
    @Autowired
    private ShoppingSessionRepository shoppingSessionRepository;

    //TODO: ShoppingSessionService
}
