package com.gardenevery.tag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import com.gardenevery.tag.key.Key;

public final class Tag<T extends Key> {

    Tag() {
    }

    private final Object2ReferenceOpenHashMap<String, ObjectOpenHashSet<T>> tagToKeys = new Object2ReferenceOpenHashMap<>();
    private final Object2ReferenceOpenHashMap<T, ObjectOpenHashSet<String>> keyToTags = new Object2ReferenceOpenHashMap<>();

    @Nonnull
    Set<T> getKeys(@Nonnull String tag) {
        var keys = tagToKeys.get(tag);
        return keys != null ? Collections.unmodifiableSet(keys) : Collections.emptySet();
    }

    @Nonnull
    Set<String> getTags(@Nonnull T key) {
        var tags = keyToTags.get(key);
        return tags != null ? Collections.unmodifiableSet(tags) : Collections.emptySet();
    }

    boolean hasTag(@Nonnull T key, @Nonnull String tagName) {
        var tags = keyToTags.get(key);
        return tags != null && tags.contains(tagName);
    }

    boolean hasAnyTag(@Nonnull T key, @Nonnull Set<String> tagNames) {
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

    boolean hasAnyTag(@Nonnull T key, @Nonnull String... tagNames) {
        if (tagNames.length == 0) {
            return false;
        }
        Set<String> tagSet = new HashSet<>();
        for (var tag : tagNames) {
            if (tag != null) {
                tagSet.add(tag);
            }
        }
        return hasAnyTag(key, tagSet);
    }

    void createTag(@Nonnull String tag, @Nonnull T key) {
        tagToKeys.computeIfAbsent(tag, k -> new ObjectOpenHashSet<>()).add(key);
        keyToTags.computeIfAbsent(key, k -> new ObjectOpenHashSet<>()).add(tag);
    }

    void createTags(@Nonnull Set<String> tags, @Nonnull T key) {
        tags.forEach(tag -> tagToKeys.computeIfAbsent(tag, k -> new ObjectOpenHashSet<>()).add(key));
        keyToTags.computeIfAbsent(key, k -> new ObjectOpenHashSet<>()).addAll(tags);
    }

    void removeTag(@Nonnull String tag) {
        var keys = tagToKeys.remove(tag);
        if (keys == null) {
            return;
        }

        for (T key : keys) {
            var tagsForKey = keyToTags.get(key);
            if (tagsForKey != null) {
                tagsForKey.remove(tag);
                if (tagsForKey.isEmpty()) {
                    keyToTags.remove(key);
                }
            }
        }
    }

    void removeTags(@Nonnull Set<String> tags) {
        tags.forEach(tag -> {
            var keys = tagToKeys.remove(tag);
            if (keys != null) {
                keys.forEach(key -> {
                    var tagsForKey = keyToTags.get(key);
                    if (tagsForKey != null) {
                        tagsForKey.remove(tag);
                        if (tagsForKey.isEmpty()) {
                            keyToTags.remove(key);
                        }
                    }
                });
            }
        });
    }

    @Nonnull
    Set<String> getAllTags() {
        return Collections.unmodifiableSet(tagToKeys.keySet());
    }

    boolean doesTagNameExist(@Nonnull String tagName) {
        return tagToKeys.containsKey(tagName);
    }

    int getTagCount() {
        return tagToKeys.size();
    }

    int getUniqueKeyCount() {
        return keyToTags.size();
    }

    int getTotalAssociations() {
        int count = 0;
        for (var entry : tagToKeys.entrySet()) {
            count += entry.getValue().size();
        }
        return count;
    }
}
