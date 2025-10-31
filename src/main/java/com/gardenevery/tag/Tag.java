package com.gardenevery.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import com.gardenevery.tag.key.Key;

public final class Tag<T extends Key> {

    Tag() {
    }

    private final Object2ReferenceOpenHashMap<String, ObjectOpenHashSet<T>> tagToKeys = new Object2ReferenceOpenHashMap<>();
    private final Object2ReferenceOpenHashMap<T, ObjectOpenHashSet<String>> keyToTags = new Object2ReferenceOpenHashMap<>();

    Set<T> getKeys(String tag) {
        var keys = tagToKeys.get(tag);
        return keys != null ? Collections.unmodifiableSet(keys) : Collections.emptySet();
    }

    Set<String> getTags(T key) {
        var tags = keyToTags.get(key);
        return tags != null ? Collections.unmodifiableSet(tags) : Collections.emptySet();
    }

    boolean hasTag(T key, String tagName) {
        var tags = keyToTags.get(key);
        return tags != null && tags.contains(tagName);
    }

    boolean hasAnyTag(T key, Collection<String> tagNames) {
        var tags = keyToTags.get(key);
        if (tags == null) {
            return false;
        }
        for (var tagName : tagNames) {
            if (tags.contains(tagName)) {
                return true;
            }
        }
        return false;
    }

    boolean hasAnyTag(T key, String... tagNames) {
        var tags = keyToTags.get(key);
        if (tags == null) {
            return false;
        }
        for (var tagName : tagNames) {
            if (tags.contains(tagName)) {
                return true;
            }
        }
        return false;
    }

    void createTag(String tag, T key) {
        tagToKeys.computeIfAbsent(tag, k -> new ObjectOpenHashSet<>()).add(key);
        keyToTags.computeIfAbsent(key, k -> new ObjectOpenHashSet<>()).add(tag);
    }

    Set<String> getAllTagNames() {
        return Collections.unmodifiableSet(tagToKeys.keySet());
    }

    boolean doesTagNameExist(String tagName) {
        return tagToKeys.containsKey(tagName);
    }

    int getTagCount() {
        return tagToKeys.size();
    }
}
