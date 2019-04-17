package no.nav.xmlstilling.ws.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.*;

public class AuthoritiesMapper implements GrantedAuthoritiesMapper {

    private static final Logger log = LoggerFactory.getLogger(AuthoritiesMapper.class);
    private Map<String, Set<ApplicationRole>> groupRoleMap;

    public AuthoritiesMapper() {
        groupRoleMap = new HashMap<>();
    }

    private void initGroupRoleMapping() {
        Set<ApplicationRole> applicationRoles = EnumSet.allOf(ApplicationRole.class);

        for (ApplicationRole applicationRole : applicationRoles) {
            // Get a comma separated list of LDAP group names that have the current role from the runtime container
            String groupString = System.getProperty(applicationRole.name() + ".groups");
            log.debug("1. Looking up ldap groups for role through property " + applicationRole.name() + ".groups, found: " + groupString);
            String groupString2 = System.getenv(applicationRole.name() + ".groups");
            log.debug("2. Looking up ldap groups for role through property " + applicationRole.name() + ".groups, found: " + groupString2);

            String groupString3 = System.getProperty(applicationRole.name() + "_groups");
            log.debug("3. Looking up ldap groups for role through property " + applicationRole.name() + "_groups, found: " + groupString3);
            String groupString4 = System.getenv(applicationRole.name() + "_groups");
            log.debug("4. Looking up ldap groups for role through property " + applicationRole.name() + "_groups, found: " + groupString4);

            if (groupString != null) {
                log.debug(String.format("Application role %s is mapped to the following LDAP groups %s", applicationRole.name(), groupString));
                addGroupRoleMapping(groupString, applicationRole);
            }
        }
    }

    /**
     * Creates a reverse Map on the format ldapGroupName=ApplicationRole1,ApplicationRole2 etc This makes it faster and easier
     * to compare with the group names (authorities) from LDAP passed in to the mapAuthorities method
     */
    private void addGroupRoleMapping(String groupString, ApplicationRole applicationRole) {
        for (String ldapGroup : Arrays.asList(groupString.split(","))) {
            String ldapGroupName = ldapGroup.trim().toLowerCase();
            if (!groupRoleMap.containsKey(ldapGroupName)) {
                groupRoleMap.put(ldapGroupName, new HashSet<>());
            }
            groupRoleMap.get(ldapGroupName).add(applicationRole);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> userGroups) {
        Set<GrantedAuthority> roles = new HashSet<>();
        initGroupRoleMapping();

        roles.add(ApplicationRole.ROLE_USER);
        for (GrantedAuthority group : userGroups) {
            String groupName = group.getAuthority().toLowerCase();
            if (groupRoleMap.containsKey(groupName)) {
                roles.addAll(groupRoleMap.get(groupName));
            }
        }

        return roles;
    }
}
