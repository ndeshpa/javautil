package org.arp.javautil.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;

public class DatabaseMap<K, V> implements Map<K, V> {

    private final Database db;
    private final StoredMap<K,V> storedMap;

    @SuppressWarnings("unchecked")
    public DatabaseMap() throws DatabaseError {
        try {
            String dbName = UUID.randomUUID().toString();
            this.db = CacheDatabase.createDatabase(dbName);

            ClassCatalog catalog = CacheDatabase.createOrGetClassCatalog();
            EntryBinding<K> kBinding = new SerialBinding<K>(catalog, null);
            EntryBinding<V> vBinding = new SerialBinding<V>(catalog, null);
            storedMap = new StoredMap<K, V>(db, kBinding, vBinding, true);
        } catch (DatabaseException dbe) {
            throw new DatabaseError(dbe);
        }
    }

    public void shutdown() throws DatabaseException {
        this.db.close();
    }

    @Override
    public void clear() {
        this.storedMap.clear();
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsKey(Object arg0) {
        return this.storedMap.containsKey(arg0);
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsValue(Object arg0) {
        return this.storedMap.containsValue(arg0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return this.storedMap.entrySet();
    }

    @SuppressWarnings("element-type-mismatch")
    @Override
    public V get(Object arg0) {
        return this.storedMap.get(arg0);
    }

    @Override
    public boolean isEmpty() {
        return this.storedMap.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return this.storedMap.keySet();
    }

    @Override
    public V put(K arg0, V arg1) {
        V old = this.storedMap.put(arg0, arg1);
        return old;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> arg0) {
        this.storedMap.putAll(arg0);
    }

    @Override
    public V remove(Object arg0) {
        @SuppressWarnings("element-type-mismatch")
        V old = this.storedMap.remove(arg0);
        return old;
    }

    @Override
    public int size() {
        return this.storedMap.size();
    }
    
    @Override
    public Collection<V> values() {
        return this.storedMap.values();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final DatabaseMap<K, V> other = (DatabaseMap<K, V>) obj;
        if (this.storedMap != other.storedMap && 
                (this.storedMap == null
                || !this.storedMap.equals(other.storedMap))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.storedMap.hashCode();
    }
}
