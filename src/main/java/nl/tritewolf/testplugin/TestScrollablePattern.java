package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.Pattern;
import nl.tritewolf.tritemenus.scrollable.ScrollableDirectionPattern;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Pattern
public class TestScrollablePattern implements ScrollableDirectionPattern {

    //    @Override
    //    public @NotNull Direction direction() {
    //        return Direction.fromScrollableDirection(Direction.VERTICALLY);
    //    }

    @Override
    public @NotNull List<@NotNull String> getPattern() {
        return List.of(
                "01|03|04|##|05|06|10|11|12|##|##|##|14",
                "##|02|07|08|##|09|##|##|##|##|##|13|15",
                "16|18|19|##|20|21|25|26|27|##|##|##|29",
                "##|17|22|23|##|24|##|##|##|##|##|28|30",
                "31|33|34|##|35|36|40|41|42|##|##|##|44",
                "##|32|37|38|##|39|##|##|##|##|##|43|45",
                "46|48|49|##|50|51|55|56|57|##|##|##|49",
                "##|47|52|53|##|54|##|##|##|##|##|58|60",
                "61|63|64|##|65|66|70|71|72|##|##|##|74",
                "##|62|67|68|##|69|##|##|##|##|##|73|75"
        );

        //        return List.of(
        //                "01|##|09|10",
        //                "02|##|08|##",
        //                "03|##|07|##",
        //                "04|05|06|##"
        //        );

        //        return List.of(
        //                "01|02|03|04|05|06|07|08|09",
        //                "##|##|##|##|##|##|##|##|10",
        //                "19|18|17|16|15|14|13|12|11",
        //                "20|##|##|##|##|##|##|##|##"
        //        );
    }
}