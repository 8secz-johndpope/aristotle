package com.aristotle.member.ui.login;

import com.google.common.collect.ImmutableSet;

import com.librishuffle.SuggestBox;
import com.librishuffle.shared.SuggestionDto;
import com.vaadin.ui.Notification;

import java.util.Set;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static java.util.stream.Collectors.toSet;

/**
 * @author Bernd Hopp
 * @version 1.0
 * CitySuggestBox is a sample SuggestBox that will suggest cities on user input
 * */
public class CitySuggestBox extends SuggestBox{

    public CitySuggestBox(){
        super("german cities here", 800, 1);
    }

    //this is the list of cities that a user can choose from
    private final Set<SuggestionDto> CITY_SUGGESTIONS = ImmutableSet.of(
            new SuggestionDto(1, "Berlin"),
            new SuggestionDto(2, "Bochum"),
            new SuggestionDto(3, "Hamburg"),
            new SuggestionDto(4, "München"),
            new SuggestionDto(5, "Köln")
    );

    @Override
    protected Set<SuggestionDto> getSuggestions(String query) {

        final String queryLower = query.toLowerCase();

        return CITY_SUGGESTIONS
                .stream()
                .filter(suggestion -> suggestion.getDisplayString().toLowerCase().contains(queryLower))
                .collect(toSet());
    }

    @Override
    public void selectionChanged(int itemId) {
        SuggestionDto citySuggestion = CITY_SUGGESTIONS
                                                .stream()
                                                .filter(suggestion -> suggestion.getItemId() == itemId)
                                                .findFirst()
                                                .get();

        Notification.show("selected " + citySuggestion.getDisplayString(), TRAY_NOTIFICATION);
    }
}