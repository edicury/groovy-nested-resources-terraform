package domain.template


import domain.entities.APIResource

class APIGatewayResource implements Template {
    @Override
    String getTemplate(String[] paths, domain.entities.Credentials credentials, APIResource resource) {

        String template = '''
            provider "aws" {
                access_key = "''' + credentials.accessKey + '''"
                secret_key = "''' + credentials.secretKey + '''"
                region = "''' + credentials.region + '''"
            }
            
            // Creates API
            resource "aws_api_gateway_rest_api" "api" {
                name        = "''' + resource.name + '''"
                description = "API Gateway created with Terraform"
            }
      
      
            // Creates root resource
            resource "aws_api_gateway_resource" "root-resource" {
                rest_api_id = "${aws_api_gateway_rest_api.api.id}"
                path_part   = "''' + paths[0] + '''"
                parent_id = "${aws_api_gateway_rest_api.api.root_resource_id}"
            }
        '''


        for(int i = 1; i < paths.length; i ++) {
            String last = i - 1 >= 1 ? paths[i - 1] : ""
            template += this.addResource(paths[i], i, last)
        }
        return template
    }

    private boolean isProxy (String path) {
        return path.equals("{proxy+}")
    }

    private addResource(String path, int index, String last) {
        String fixedPath = path

        if(this.isProxy(path)) {
            fixedPath = path.replace("+", "_proxied").replace("{", "").replace("}", "")
        }


        return ''' 
            resource "aws_api_gateway_resource" "resource-''' + fixedPath + '''" {
                rest_api_id = "${aws_api_gateway_rest_api.api.id}"
                path_part   = "''' + path + '''"
                parent_id = "''' + (index == 1 ? "\${aws_api_gateway_resource.root-resource.id}" : "\${aws_api_gateway_resource.resource-" + last + ".id}") + '''"
            }
        '''
    }
}
