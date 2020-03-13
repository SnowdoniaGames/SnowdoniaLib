package com.snowdonia.lib.cards;

import javafx.scene.layout.BorderPane;

public class Card
{
	private String resourceLoc;
	private CardController controller;
	private BorderPane pane;

	public Card(String resourceLoc, CardController controller, BorderPane pane)
	{
		this.resourceLoc = resourceLoc;
		this.controller = controller;
		this.pane = pane;
	}

	public String getResourceLoc()
	{
		return resourceLoc;
	}

	public CardController getController()
	{
		return controller;
	}

	public BorderPane getPane()
	{
		return pane;
	}
}
