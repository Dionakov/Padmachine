package org.neotouch.neopad.view;

import java.util.Observer;

import org.neotouch.neopad.model.AbstractModel;

public interface ViewController extends Observer
{
	void addModel(AbstractModel model);
}
