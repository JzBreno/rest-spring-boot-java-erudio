package br.com.jzbreno.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "user_name", nullable = false, length = 80)
    private String username;
    @Column(nullable = false,name = "password", length = 255)
    private String password;
    @Column(name = "full_name", length = 80)
    private String fullName;
    @Column(name = "account_non_expired", length = 80)
    private Boolean acountNonExpired;
    @Column(name = "account_non_locked", length = 80)
    private Boolean accountNonLocked;
    @Column(name = "credentials_non_expired", length = 80)
    private Boolean credentialsNonExpired;
    @Column(name = "enabled", length = 80)
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER) // trazer todas as permissoes de tabelas terceiras junto a primeira requisicao
    @JoinTable(name = "user_permission", // tabela do meio
    joinColumns = @JoinColumn(name = "id_user"),
    inverseJoinColumns = {@JoinColumn(name = "id_permission")})
    private List<Permission> permissions;

    public List<String> getRoles(){
        List<String> roles = List.of(permissions.stream().map(Permission::getDescription).toList().toArray(String[]::new));
        return roles;
    }

    public User() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(fullName, user.fullName) && Objects.equals(acountNonExpired, user.acountNonExpired) && Objects.equals(accountNonLocked, user.accountNonLocked) && Objects.equals(credentialsNonExpired, user.credentialsNonExpired) && Objects.equals(enabled, user.enabled) && Objects.equals(permissions, user.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, fullName, acountNonExpired, accountNonLocked, credentialsNonExpired, enabled, permissions);
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.acountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getAcountNonExpired() {
        return acountNonExpired;
    }

    public void setAcountNonExpired(Boolean acountNonExpired) {
        this.acountNonExpired = acountNonExpired;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }
}
