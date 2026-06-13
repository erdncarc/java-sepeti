package com.example.javasepeti.service;

import com.example.javasepeti.dto.customer.AddCardRequest;
import com.example.javasepeti.dto.UpdateCardRequest;
import com.example.javasepeti.model.Card;
import com.example.javasepeti.model.Customer;
import com.example.javasepeti.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    protected final CardRepository cardRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public CardService(CardRepository cardRepository, ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
    }


    public Card findById(Long cardId){
        return cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Card with id: %s not found",cardId))
        );
    }

    public Card findByIdAndCustomer(Long cardId, Customer customer){
        return cardRepository.findByCardIdAndCustomer(cardId, customer).orElseThrow(
                () -> new EntityNotFoundException(String.format("Card with id: %s related with customer: %s not found",cardId, customer.getEmail()))
        );
    }

    public Card add(Customer customer, AddCardRequest addCardRequest){
        Card card = modelMapper.map(addCardRequest, Card.class);
        card.setCustomer(customer);
        return save(card);
    }

    public Card update(Customer customer, UpdateCardRequest updateCardrequest){
        Card card = findByIdAndCustomer(updateCardrequest.getCardId(), customer);
        modelMapper.map(updateCardrequest,card);
        return this.save(card);
    }

    public Card save(Card card){
        return cardRepository.save(card);
    }

    public void delete(Customer customer, Long cardId){
        Card card = findByIdAndCustomer(cardId, customer);
        cardRepository.delete(card);
    }

}
