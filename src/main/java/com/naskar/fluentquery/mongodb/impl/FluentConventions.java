package com.naskar.fluentquery.mongodb.impl;

import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.PropertyModelBuilder;

public class FluentConventions implements Convention {

	@Override
	public void apply(ClassModelBuilder<?> classModelBuilder) {
		for (final PropertyModelBuilder<?> propertyModel : classModelBuilder.getPropertyModelBuilders()) {
            if (classModelBuilder.getIdPropertyName() == null) {
                String propertyName = propertyModel.getName();
                if (propertyName.equals("id")) {
                    classModelBuilder.idPropertyName(propertyName);
                }
            }
        }
	}

}
