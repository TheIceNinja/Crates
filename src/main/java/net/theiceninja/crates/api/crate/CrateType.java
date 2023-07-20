package net.theiceninja.crates.api.crate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CrateType {

    RARE("&#3BD864&lRare"),
    EPIC("&#FAB3FF&lEpic"),
    LEGENDARY("&#F8CE4B&lLegendary"),
    VOTE("&#A2FF99&lVote");


    private final String prefix;
}
