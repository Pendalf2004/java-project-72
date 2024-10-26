package DataTemplate.Paths;

import DataTemplate.BasePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import model.UrlModel;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class RootPage extends BasePage {
    private List<UrlModel> urls;
}
