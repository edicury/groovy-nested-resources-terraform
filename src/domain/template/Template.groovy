package domain.template


import domain.entities.APIResource

interface Template {
    String getTemplate(String[] paths, domain.entities.Credentials credentials, APIResource resource)
}
