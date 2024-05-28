`New-AzResource`  部署单一资源

```bash
New-AzResource -ResourceName "mystorageaccount" -ResourceGroupName "myResourceGroup" -ResourceType "Microsoft.Storage/storageAccounts" -Location "EastUS" -Properties @{}
```

`New-AzResourceGroupDeployment`  在resource group 里部署资源若干个资源

```bash
New-AzResourceGroupDeployment -ResourceGroupName "myResourceGroup" -TemplateFile "azuredeploy.json" -TemplateParameterFile "azuredeploy.parameters.json"
```

`New-AzSubscriptionDeployment` = `New-AzDeployment` 在当前 subscription 里部署资源。[["]](https://learn.microsoft.com/en-us/azure/azure-resource-manager/bicep/deploy-powershell)

```bash
New-AzDeployment -Location "West US" -TemplateFile "D:\Azure\Templates\EngineeringSite.json" -TemplateParameterFile "D:\Azure\Templates\EngSiteParms.json" -Tag @{"key1"="value1"; "key2"="value2";}
```

`New-AzManagementGroupDeployment` 在 management grope 里部署资源