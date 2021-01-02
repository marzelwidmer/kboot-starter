
# Run & Watch 
opa run -s -w authz.rego


# Run as Stubs
```bash
cd spring-cli 
spring cloud stubrunner
```
Test stubs
```bash 
http POST :8181/v1/data/http/authz/allow @input.json
```

```bash
http POST :8181/v1/data/http/authz/allow <<<'{"input":{"name":"john.doe@keepcalm.ch","authorities":[{"authority":"keepcalm.user"}],"method":"GET","path":["","api","user","foo"]}}'
```
