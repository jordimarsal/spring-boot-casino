package net.jordimp.casino.services.factory;

public interface AbstractFactory<T> {
	T create(String gameType);
}
