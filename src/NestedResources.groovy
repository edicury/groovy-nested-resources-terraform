package groovy.lang

import domain.template.APIGatewayResource
import domain.entities.APIResource
import domain.entities.Credentials

class NestedResources {
    void run() {
        Console console = new Console()
        String accessKey = console.readLine("AWS ACCESS ID: ")

        String secretKey = console.readLine("AWS SECRET KEY: ")
        String region = console.readLine("AWS REGION: ")

        Credentials credentials = new Credentials(accessKey, secretKey, region)

        String apiResource = console.readLine("API name: ")

        APIResource resource = new APIResource(apiResource)

        String path = console.readLine("Path: ")

        String[] paths = path.split("/")

        String template = new APIGatewayResource().getTemplate(paths, credentials, resource)
        File mainTf = new File('./tf/main.tf')
        mainTf.write(template)


        println("Template created")
        println(template)
    }
}
