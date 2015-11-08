package be.gling.android.view;

import be.gling.android.model.dto.technical.DTO;

/**
 * Created by florian on 4/12/14.
 */
public interface RequestActionInterface {

    public void displayErrorMessage(String errorMessage);

    public void loadingAction(boolean loading);

    public void successAction(DTO successDTO);
}
