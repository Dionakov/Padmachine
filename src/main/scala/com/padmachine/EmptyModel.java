package org.neotouch.neopad.model;

/**
 * Useless model used for testing the View module.
 * 
 * @author Roch Dionnet
 */
public final class EmptyModel extends AbstractModel
{
	private boolean testButtonClicked = true;

	public boolean isTestButtonClicked()
	{
		return testButtonClicked;
	}

	public void setTestButtonClicked(boolean testButtonClicked)
	{
		this.testButtonClicked = testButtonClicked;
		setChanged();
		notifyObservers();
	}
}
