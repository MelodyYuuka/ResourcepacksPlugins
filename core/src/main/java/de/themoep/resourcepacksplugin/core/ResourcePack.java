package de.themoep.resourcepacksplugin.core;

/*
 * ResourcepacksPlugins - core
 * Copyright (C) 2018 Max Lee aka Phoenix616 (mail@moep.tv)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.google.common.io.BaseEncoding;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Phoenix616 on 25.03.2015.
 */
public class ResourcePack {
    private final String name;
    private UUID uuid;
    private String url;
    private String localPath;
    private byte[] hash = new byte[0];
    private int format;
    private int version;
    private boolean restricted;
    private String permission;
    private ClientType type;

    private final ResourcePack parent;
    private final List<ResourcePack> variants = new ArrayList<>();

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an unique identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     */
    public ResourcePack(String name, String url, String hash) {
        this(name, url, hash, 0);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an unique identifier. Correct case.
     * @param uuid The uuid of the resourcepack as set in the config.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     */
    public ResourcePack(String name, UUID uuid, String url, String hash) {
        this(name, url, hash, 0);
        this.uuid = uuid;
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an unique identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta
     */
    public ResourcePack(String name, String url, String hash, int format) {
        this(name, url, hash, format, false);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an unique identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     */
    public ResourcePack(String name, String url, String hash, boolean restricted) {
        this(name, url, hash, 0, restricted);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an unique identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     */
    public ResourcePack(String name, String url, String hash, int format, boolean restricted) {
        this(name, url, hash, format, restricted, "resourcepacksplugin.pack." + name.toLowerCase(Locale.ROOT));
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     */
    public ResourcePack(String name, String url, String hash, int format, boolean restricted, String permission) {
        this(name, url, hash, format, 0, restricted, permission);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param version The Minecraft version that this resourcepack is for
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     */
    public ResourcePack(String name, String url, String hash, int format, int version, boolean restricted, String permission) {
        this(name, url, hash, format, version, restricted, permission, ClientType.ORIGINAL);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param version The Minecraft version that this resourcepack is for
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     * @param type The type of the pack depending on the client which should receive it
     */
    public ResourcePack(String name, String url, String hash, int format, int version, boolean restricted, String permission, ClientType type) {
        this(name, url, hash, null, format, version, restricted, permission, type);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an unique identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param localPath The local path to this resourcepack. Ideally this points to the same file as the url points to.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param version The Minecraft version that this resourcepack is for
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     * @param type The type of the pack depending on the client which should receive it
     */
    public ResourcePack(String name, String url, String hash, String localPath, int format, int version, boolean restricted, String permission, ClientType type) {
        this(name, UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8)), url, hash, localPath, format, version, restricted, permission, type);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an unique identifier. Correct case.
     * @param uuid The uuid of the resourcepack as set in the config.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param localPath The local path to this resourcepack. Ideally this points to the same file as the url points to.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param version The Minecraft version that this resourcepack is for
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     * @param type The type of the pack depending on the client which should receive it
     */
    public ResourcePack(String name, UUID uuid, String url, String hash, String localPath, int format, int version, boolean restricted, String permission, ClientType type) {
        this(null, name, uuid, url, hash, localPath, format, version, restricted, permission, type);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param parent The parent pack if this is a variant of another pack. Can be null if this is not a variant.
     * @param name The name of the resourcepack as set in the config. Serves as an unique identifier. Correct case.
     * @param uuid The uuid of the resourcepack as set in the config.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param localPath The local path to this resourcepack. Ideally this points to the same file as the url points to.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param version The Minecraft version that this resourcepack is for
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     * @param type The type of the pack depending on the client which should receive it
     */
    public ResourcePack(ResourcePack parent, String name, UUID uuid, String url, String hash, String localPath, int format, int version, boolean restricted, String permission, ClientType type) {
        this.parent = parent;
        this.name = name;
        this.uuid = uuid;
        this.url = url;
        if (hash != null && !hash.isEmpty() && !"null".equals(hash)) {
            setHash(hash);
        }
        this.localPath = localPath;
        this.format = format;
        this.version = version;
        this.restricted = restricted;
        this.permission = permission;
        this.type = type;
    }
    
    /**
     * Get the name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @return The name as a string in correct case
     */
    public String getName() {
        return name;
    }

    /**
     * Get the universally unique identifier (UUID) associated with the resource pack.
     * @return The UUID as an instance of the UUID class.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Set the universally unique identifier (UUID) associated with the resource pack.
     * @param uuid The UUID of the resource pack.
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the url where this resourcepack is located at and where the client will download it from
     * @return The url as a string
     */
    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @return The 40 digit lowercase hash
     */
    public String getHash() {
        return BaseEncoding.base16().lowerCase().encode(hash);
    }

    void setHash(String hash) {
        setRawHash(BaseEncoding.base16().lowerCase().decode(hash.toLowerCase(Locale.ROOT)));
    }

    public byte[] getRawHash() {
        return hash;
    }

    public void setRawHash(byte[] hash) {
        if (hash.length > 0 && hash.length != 20) {
            throw new IllegalArgumentException("Hash needs to be either 0 or 20 bytes long!");
        }
        this.hash = hash;
    }

    /**
     * Get the local path where this resourcepack is located at on your file system
     * @return The path as a string
     */
    public String getLocalPath() {
        return localPath;
    }

    /**
     * Get the local path where this resourcepack is located at on your file system
     * @param localPath The path as a string. How this needs to be formatted depends on your system.
     */
    void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    /**
     * Get the pack format version
     * @return The pack version as an int
     */
    public int getFormat() {
        return format;
    }
    /**
     * Set the pack format version
     * @param format The pack version as an int
     * @return Whether or not the format changed
     */
    public boolean setFormat(int format) {
        if (this.format == format) {
            return false;
        }
        this.format = format;
        return true;
    }

    /**
     * Get the pack Minecraft protocol version
     * @return The Minecraft protocol version as an int
     */
    public int getVersion() {
        return version;
    }

    /**
     * Set the pack Minecraft protocol version
     * @param version The Minecraft protocol version as an int
     */
    public boolean setVersion(int version) {
        if (this.version == version) {
            return false;
        }
        this.version = version;
        return true;
    }

    /**
     * Utility method to set the pack Minecraft version from a string
     * @param versionString The Minecraft version as string descriptor
     * @throws IllegalArgumentException Thrown when the string is not valid
     */
    public boolean setVersion(String versionString) {
        int mcVersion = 0;
        try {
            mcVersion = MinecraftVersion.parseVersion(versionString).getProtocolNumber();
        } catch (IllegalArgumentException e) {
            try {
                mcVersion = Integer.parseInt(versionString);
            } catch (NumberFormatException e1) {
                throw new IllegalArgumentException("'" + versionString + "' is not a valid Minecraft version string nor protocol number");
            }
        }
        return setVersion(mcVersion);
    }

    /**
     * Whether or not this pack is restricted and a permission should be used
     * @return <code>true</code> if one needs the permission, <code>false</code> if not
     */
    public boolean isRestricted() {
        return restricted;
    }

    /**
     * Whether or not this pack is restricted and a permission should be used
     * @param restricted <code>true</code> if one needs the permission, <code>false</code> if not
     * @return Whether or not the restricted status changed
     */
    public boolean setRestricted(boolean restricted) {
        if (this.restricted == restricted) {
            return false;
        }
        this.restricted = restricted;
        return true;
    }

    /**
     * Get the permission to use this pack
     * @return The permission as a string
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Set the permission to use this pack
     * @param permission The permission as a string
     * @return Whether or not the permission changed
     */
    public boolean setPermission(String permission) {
        if (this.permission.equals(permission)) {
            return false;
        }
        this.permission = permission;
        return true;
    }

    /**
     * Get the client type that the resource pack is for
     * @return The type
     */
    public ClientType getType() {
        return type;
    }

    /**
     * Set the client type that the resource pack is for
     * @param type The type
     * @return Whether or not the ClientType changed
     */
    public boolean setType(ClientType type) {
        if (this.type == type) {
            return false;
        }
        this.type = type;
        return true;
    }

    /**
     * Get the parent pack if this is a variant of another pack.
     * @return The parent pack or null if this is not a variant
     */
    public ResourcePack getParent() {
        return parent;
    }

    /**
     * Helper method to get the root parent of this pack.
     * @return The root parent pack, which is the first pack in the chain of parents. If this pack has no parent, it will return itself.
     */
    public ResourcePack getRootParent() {
        ResourcePack root = this;
        while (root.getParent() != null) {
            root = root.getParent();
        }
        return root;
    }

    /**
     * Get a list of different pack variants. Used to get, add and remove variants.
     * @return The list of pack variants
     */
    public List<ResourcePack> getVariants() {
        return variants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourcePack pack = (ResourcePack) o;
        return format == pack.format && version == pack.version && restricted == pack.restricted
                && Objects.equals(name, pack.name) && Objects.equals(uuid, pack.uuid)
                && Objects.equals(url, pack.url) && Objects.equals(localPath, pack.localPath)
                && Arrays.equals(hash, pack.hash) && Objects.equals(permission, pack.permission)
                && type == pack.type && Objects.equals(variants, pack.variants);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, uuid, url, localPath, format, version, restricted, permission, type, variants);
        result = 31 * result + Arrays.hashCode(hash);
        return result;
    }

    public String[] getReplacements() {
        return new String[] {
                "name", getName(),
                "uuid", getUuid() != null ? getUuid().toString() : "-",
                "url", getUrl() != null ? getUrl() : "-",
                "hash", getRawHash() != null ? getHash() : "-",
                "format", String.valueOf(getFormat()),
                "version", String.valueOf(getVersion()),
                "restricted", String.valueOf(isRestricted()),
                "permission", getPermission(),
                "type", getType().humanName(),
                "parent", getParent() != null ? getParent().getName() : "-",
                "variants", String.valueOf(getVariants().size())
        };
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("url", url == null || url.isEmpty() || !variants.isEmpty()  ? null : url);
        map.put("uuid", uuid == null || !variants.isEmpty() ? null : uuid.toString());
        map.put("hash", hash == null || hash.length == 0 || !variants.isEmpty()  ? null : getHash());
        map.put("local-path", localPath == null || localPath.isEmpty() || !variants.isEmpty() ? null : localPath);
        if (name.equalsIgnoreCase(PackManager.EMPTY_IDENTIFIER)) {
            map.put("format", null);
            map.put("version", null);
            map.put("restricted", null);
            map.put("permission", null);
        } else {
            map.put("format", format > 0 ? format : null);
            if (version > 0) {
                MinecraftVersion mcVersion = MinecraftVersion.getExactVersion(version);
                map.put("version", mcVersion != null ? mcVersion.toConfigString() : version);
            } else {
                map.put("version", null);
            }
            map.put("restricted", restricted);
            map.put("permission", permission);
            if (type == ClientType.ORIGINAL) {
                map.put("type", null);
            } else {
                map.put("type", type.name().toLowerCase(Locale.ROOT));
            }
        }
        if (variants.isEmpty()) {
            map.put("variants", null);
        } else {
            List<Map<String, Object>> variantsList = new ArrayList<>();
            for (ResourcePack packVariant : variants) {
                variantsList.add(packVariant.serialize());
            }
            map.put("variants", variantsList);
        }

        return map;
    }
}
