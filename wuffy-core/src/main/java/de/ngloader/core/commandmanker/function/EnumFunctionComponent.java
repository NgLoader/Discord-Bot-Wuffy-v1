package de.ngloader.core.commandmanker.function;

import de.ngloader.core.event.WuffyMessageRecivedEvent;

public enum EnumFunctionComponent {

	WUFFYMESSAGERECIVEDEVENT(WuffyMessageRecivedEvent.class),
	EQUALS(String.class, String.class),
	NOT_EQUALS(String.class, String.class),
	NULL;

	public final Object[] supportedArgs;

	private EnumFunctionComponent(Object... supportedArgs) {
		this.supportedArgs = supportedArgs;
	}

	public Object[] getSupportedArgs() {
		return this.supportedArgs;
	}
}