package com.github.zly2006.craftminefixes;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ItemStackIterator implements Iterator<ItemStack> {
    private final Iterator<ItemStack> rootIterator;
    private ItemStackIterator currentIterator;

    public ItemStackIterator(Iterator<ItemStack> rootIterator) {
        this.rootIterator = rootIterator;
        currentIterator = null;
    }

    @Override
    public boolean hasNext() {
        return currentIterator == null
                ? rootIterator.hasNext()
                : rootIterator.hasNext() || currentIterator.hasNext();
    }

    @Override
    public ItemStack next() {
        if (currentIterator != null) {
            if (currentIterator.hasNext()) {
                return currentIterator.next();
            } else if (rootIterator.hasNext()) {
                currentIterator = null;
            }
        }
        if (rootIterator.hasNext()) {
            ItemStack itemStack = rootIterator.next();
            Iterator<ItemStack> iterator = getIterator(itemStack);
            if (iterator != null && iterator.hasNext()) {
                currentIterator = new ItemStackIterator(iterator);
                return currentIterator.next();
            } else {
                return itemStack;
            }
        } else {
            throw new NoSuchElementException("Already reached the end of the iterator");
        }
    }

    private Iterator<ItemStack> getIterator(ItemStack stack) {
        BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            return bundleContents.items().iterator();
        }
        ItemContainerContents containerContents = stack.get(DataComponents.CONTAINER);
        if (containerContents != null) {
            return containerContents.stream().iterator();
        }
        return null;
    }
}
