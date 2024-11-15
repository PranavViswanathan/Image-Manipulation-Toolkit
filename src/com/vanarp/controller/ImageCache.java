package com.vanarp.controller;

import com.vanarp.model.ImageRepresentation;

import java.util.HashMap;
import java.util.Map;

public class ImageCache {
  private final Map<String, ImageRepresentation> cache;

  public ImageCache() {
    this.cache = new HashMap<>();
  }

  public void putImage(String name, ImageRepresentation image) {
    if (name == null || image == null) {
      throw new IllegalArgumentException("Name or image cannot be null");
    }
    cache.put(name, image);
  }

  public ImageRepresentation getImage(String name) {
    if (!cache.containsKey(name)) {
      throw new IllegalArgumentException("Image not found in cache: " + name);
    }
    return cache.get(name);
  }

  public void removeImage(String name) {
    if (!cache.containsKey(name)) {
      throw new IllegalArgumentException("Image not found in cache: " + name);
    }
    cache.remove(name);
  }

  public boolean containsImage(String name) {
    return cache.containsKey(name);
  }
}
