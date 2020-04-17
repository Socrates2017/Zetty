package com.zrzhen.idmaker.core;

public interface IdConverter {

	public long convert(Id id);

	public Id convert(long id);

}
