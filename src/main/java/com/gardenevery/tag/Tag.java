package com.gardenevery.tag;

import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public final class Tag<T extends Key> {

    Tag() {}

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

    void createTag(@Nonnull String tag, @Nonnull T key) {
        tagToKeys.computeIfAbsent(tag, k -> new ObjectOpenHashSet<>()).add(key);
        keyToTags.computeIfAbsent(key, k -> new ObjectOpenHashSet<>()).add(tag);
    }

    void createTags(@Nonnull Set<String> tags, @Nonnull T key) {
        ObjectSet<String> keyTags = keyToTags.computeIfAbsent(key, k -> new ObjectOpenHashSet<>());

        for (var tag : tags) {
            tagToKeys.computeIfAbsent(tag, k -> new ObjectOpenHashSet<>()).add(key);
            keyTags.add(tag);
        }
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
        for (var tag : tags) {
            var keys = tagToKeys.remove(tag);
            if (keys == null) {
                continue;
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
    }

    @Nonnull
    Set<String> getAllTags() {
        return Collections.unmodifiableSet(tagToKeys.keySet());
    }

    boolean doesTagNameExist(@Nonnull String tagName) {
        return tagToKeys.containsKey(tagName);
    }

    boolean containsKey(@Nonnull T key) {
        return keyToTags.containsKey(key);
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

    void clear() {
        tagToKeys.clear();
        keyToTags.clear();
    }
}
