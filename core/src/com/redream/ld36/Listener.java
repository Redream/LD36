package com.redream.ld36;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Listener implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		
		Object oA = contact.getFixtureA().getBody().getUserData();
		Object oB = contact.getFixtureB().getBody().getUserData();
		if(oA instanceof Beacon && oB instanceof Entity){
			Beacon ba = (Beacon) oA;
			ba.contacted((Entity) oB);
			return;
		}
		if(oB instanceof Beacon && oA instanceof Entity){
			Beacon bb = (Beacon) oB;
			bb.contacted((Entity) oA);
			return;
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse imp) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold man) {
		
	}

}
