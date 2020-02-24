package com.snowdonia.lib.cards;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.snowdonia.lib.logging.SnowdoniaLogger.logger;

public class CardManager
{
	private Card cardCurrent = null;
	private List<Card> cardMap = new ArrayList<>();

	private BorderPane paneContent;

	public CardManager(BorderPane paneContent)
	{
		this.paneContent = paneContent;
	}

	private Card getCard(String resourceLoc)
	{
		return cardMap.stream().filter(card -> card.getResourceLoc().equals(resourceLoc)).findFirst().orElse(null);
	}

	public void preload(String resourceLoc)
	{
		Card card = getCard(resourceLoc);
		if (card == null)
		{
			Object root = null;
			Object controller = null;
			try
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceLoc));
				root = loader.load();
				controller = loader.getController();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (!(root instanceof BorderPane))
			{
				logger.error(this.getClass(), "Failed to preload card, '" + resourceLoc + "' is not an instance of BorderPane");
				return;
			}

			if (!(controller instanceof CardController))
			{
				logger.error(this.getClass(), "Failed to preload card, '" + resourceLoc + "' could not be found");
				return;
			}

			//create card
			Card newCard = new Card(resourceLoc, (CardController) controller, (BorderPane) root);

			//save card
			cardMap.add(newCard);
		}
	}

	public void setCard(String resourceLoc)
	{
		if (paneContent == null)
		{
			logger.error(this.getClass(), "Failed to set card, could not find pane content");
			return;
		}

		//save current card
		if (cardCurrent != null)
		{
			cardCurrent.getController().save();
		}

		Card card = getCard(resourceLoc);
		if (card != null)
		{
			//set current
			cardCurrent = card;

			//load config
			card.getController().load();

			//set content
			paneContent.setCenter(card.getPane());
		}
		else
		{
			Object root = null;
			Object controller = null;
			try
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceLoc));
				root = loader.load();
				controller = loader.getController();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (!(root instanceof BorderPane))
			{
				logger.error(this.getClass(), "Failed to set card, '" + resourceLoc + "' is not an instance of BorderPane");
				return;
			}

			if (!(controller instanceof CardController))
			{
				logger.error(this.getClass(), "Failed to set card, '" + resourceLoc + "' could not be found");
				return;
			}

			//create card
			Card newCard = new Card(resourceLoc, (CardController) controller, (BorderPane) root);

			//set current
			cardCurrent = newCard;

			//save card
			cardMap.add(newCard);

			//load config
			((CardController) controller).load();

			//set content
			paneContent.setCenter((BorderPane) root);
		}
	}

	public void clear()
	{
		if (paneContent == null)
		{
			logger.error(this.getClass(), "Failed to set card, could not find pane content");
			return;
		}

		//save current card
		if (cardCurrent != null)
		{
			cardCurrent.getController().save();
		}

		//clear card
		cardCurrent = null;

		//clear content
		paneContent.setCenter(null);
	}
}
