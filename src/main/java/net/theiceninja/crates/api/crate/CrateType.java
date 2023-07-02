package net.theiceninja.crates.api.crate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CrateType {

    RARE("&#17F73C&lRare"),
    EPIC("&#C117F7&lEpic");

    private final String prefix;
}
