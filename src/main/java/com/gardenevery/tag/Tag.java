package com.gardenevery.tag;

import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

final class Tag<T extends Key> {

    Tag() {}

    private final Object2ReferenceOpenHashMap<String, ObjectOpenHashSet<T>> tagToKeys = new Object2ReferenceOpenHashMap<>();
    private final Object2ReferenceOpenHashMap<T, ObjectOpenHashSet<String>> keyToTags = new Object2ReferenceOpenHashMap<>();

    public Set<String> getTag(@Nonnull T key) {
        var tags = keyToTags.get(key);
        return tags != null ? Collections.unmodifiableSet(tags) : Collections.emptySet();
    }

    public Set<T> getKey(@Nonnull String tagName) {
        var keys = tagToKeys.get(tagName);
        return keys != null ? Collections.unmodifiableSet(keys) : Collections.emptySet();
    }

    public Set<String> getAllTag() {
        return Collections.unmodifiableSet(tagToKeys.keySet());
    }

    public boolean hasTag(@Nonnull T key, @Nonnull String tagName) {
        var tags = keyToTags.get(key);
        return tags != null && tags.contains(tagName);
    }

    public boolean hasAnyTag(@Nonnull T key, @Nonnull Set<String> tagNames) {
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

    public void createTag(@Nonnull String tagName, @Nonnull T key) {
        tagToKeys.computeIfAbsent(tagName, k -> new ObjectOpenHashSet<>()).add(key);
        keyToTags.computeIfAbsent(key, k -> new ObjectOpenHashSet<>()).add(tagName);
    }

    public void createTag(@Nonnull Set<String> tagNames, @Nonnull T key) {
        var tagsForKey = keyToTags.computeIfAbsent(key, k -> new ObjectOpenHashSet<>());
        tagsForKey.addAll(tagNames);

        for (var tag : tagNames) {
            tagToKeys.computeIfAbsent(tag, k -> new ObjectOpenHashSet<>()).add(key);
        }
    }

    public void removeTag(@Nonnull String tagName) {
        var keys = tagToKeys.remove(tagName);
        if (keys == null) {
            return;
        }

        for (T key : keys) {
            var tagsForKey = keyToTags.get(key);
            if (tagsForKey != null) {
                tagsForKey.remove(tagName);
                if (tagsForKey.isEmpty()) {
                    keyToTags.remove(key);
                }
            }
        }
    }

    public void removeTag(@Nonnull Set<String> tagNames) {
        var removals = new Object2ReferenceOpenHashMap<T, ObjectOpenHashSet<String>>();
        for (var tag : tagNames) {
            var keys = tagToKeys.remove(tag);
            if (keys == null) {
                continue;
            }

            for (T key : keys) {
                var set = removals.computeIfAbsent(key, k -> new ObjectOpenHashSet<>());
                set.add(tag);
            }
        }

        for (var entry : removals.object2ReferenceEntrySet()) {
            T key = entry.getKey();
            ObjectOpenHashSet<String> tagsToRemove = entry.getValue();
            var tagsForKey = keyToTags.get(key);
            if (tagsForKey != null) {
                tagsForKey.removeAll(tagsToRemove);
                if (tagsForKey.isEmpty()) {
                    keyToTags.remove(key);
                }
            }
        }
    }

    public void removeTagKey(@Nonnull String tagName, @Nonnull T key) {
        var keysForTag = tagToKeys.get(tagName);
        if (keysForTag != null) {
            keysForTag.remove(key);
            if (keysForTag.isEmpty()) {
                tagToKeys.remove(tagName);
            }
        }

        var tagsForKey = keyToTags.get(key);
        if (tagsForKey != null) {
            tagsForKey.remove(tagName);
            if (tagsForKey.isEmpty()) {
                keyToTags.remove(key);
            }
        }
    }

    public void removeTagKey(@Nonnull Set<String> tagNames, @Nonnull T key) {
        var tagsForKey = keyToTags.get(key);
        if (tagsForKey != null) {
            tagsForKey.removeAll(tagNames);
            if (tagsForKey.isEmpty()) {
                keyToTags.remove(key);
            }
        }

        for (var tagName : tagNames) {
            var keysForTag = tagToKeys.get(tagName);
            if (keysForTag != null) {
                keysForTag.remove(key);
                if (keysForTag.isEmpty()) {
                    tagToKeys.remove(tagName);
                }
            }
        }
    }

    public boolean doesTagExist(@Nonnull String tagName) {
        return tagToKeys.containsKey(tagName);
    }

    public boolean containsKey(@Nonnull T key) {
        return keyToTags.containsKey(key);
    }

    public int getTagCount() {
        return tagToKeys.size();
    }

    public int getKeyCount() {
        return keyToTags.size();
    }

    public int getAssociations() {
        int count = 0;
        for (var entry : tagToKeys.entrySet()) {
            count += entry.getValue().size();
        }
        return count;
    }
}
