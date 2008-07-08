begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|maven_plugin
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|Option
block|{
comment|/**      * As maven will set null for an empty parameter we need      * this horrid inital value to tell if it has been       * configured or not yet.      */
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_WSDL_LOCATION
init|=
literal|"@�$%^&*()DEFAULTWSDLLOCATION"
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|packagenames
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|extraargs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|File
name|outputDir
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|namespaceExcludes
decl_stmt|;
name|Boolean
name|defaultExcludesNamespace
decl_stmt|;
name|Boolean
name|defaultNamespacePackageMapping
decl_stmt|;
name|File
name|dependencies
index|[]
decl_stmt|;
name|File
name|redundantDirs
index|[]
decl_stmt|;
name|String
name|bindingFiles
index|[]
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
name|String
name|wsdlLocation
init|=
name|DEFAULT_WSDL_LOCATION
decl_stmt|;
name|String
name|frontEnd
decl_stmt|;
name|String
name|dataBinding
decl_stmt|;
name|String
name|wsdlVersion
decl_stmt|;
name|String
name|catalog
decl_stmt|;
name|boolean
name|extendedSoapHeaders
decl_stmt|;
name|boolean
name|validateWsdl
decl_stmt|;
name|String
name|serviceName
decl_stmt|;
name|boolean
name|autoNameResolution
decl_stmt|;
name|boolean
name|noAddressBinding
decl_stmt|;
specifier|public
name|Option
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExtraargs
parameter_list|()
block|{
return|return
name|extraargs
return|;
block|}
specifier|public
name|void
name|setExtraargs
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ea
parameter_list|)
block|{
name|this
operator|.
name|extraargs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|extraargs
operator|.
name|addAll
argument_list|(
name|ea
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPackagenames
parameter_list|()
block|{
return|return
name|packagenames
return|;
block|}
specifier|public
name|void
name|setPackagenames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|pn
parameter_list|)
block|{
name|this
operator|.
name|packagenames
operator|=
name|pn
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getNamespaceExcludes
parameter_list|()
block|{
return|return
name|namespaceExcludes
return|;
block|}
specifier|public
name|void
name|setNamespaceExcludes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|namespaceExcludes
parameter_list|)
block|{
name|this
operator|.
name|namespaceExcludes
operator|=
name|namespaceExcludes
expr_stmt|;
block|}
specifier|public
name|void
name|setDependencies
parameter_list|(
name|File
name|files
index|[]
parameter_list|)
block|{
name|dependencies
operator|=
name|files
expr_stmt|;
block|}
specifier|public
name|File
index|[]
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
specifier|public
name|void
name|setDeleteDirs
parameter_list|(
name|File
name|files
index|[]
parameter_list|)
block|{
name|redundantDirs
operator|=
name|files
expr_stmt|;
block|}
specifier|public
name|File
index|[]
name|getDeleteDirs
parameter_list|()
block|{
return|return
name|redundantDirs
return|;
block|}
specifier|public
name|File
name|getOutputDir
parameter_list|()
block|{
return|return
name|outputDir
return|;
block|}
specifier|public
name|void
name|setOutputDir
parameter_list|(
name|File
name|f
parameter_list|)
block|{
name|outputDir
operator|=
name|f
expr_stmt|;
block|}
specifier|public
name|void
name|setBindingFiles
parameter_list|(
name|String
name|files
index|[]
parameter_list|)
block|{
name|bindingFiles
operator|=
name|files
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getBindingFiles
parameter_list|()
block|{
return|return
name|bindingFiles
return|;
block|}
specifier|public
name|void
name|addBindingFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|tmp
index|[]
init|=
operator|new
name|String
index|[
name|bindingFiles
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bindingFiles
argument_list|,
literal|0
argument_list|,
name|tmp
argument_list|,
literal|0
argument_list|,
name|bindingFiles
operator|.
name|length
argument_list|)
expr_stmt|;
name|bindingFiles
operator|=
name|tmp
expr_stmt|;
name|bindingFiles
index|[
name|bindingFiles
operator|.
name|length
operator|-
literal|1
index|]
operator|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setWsdlLocation
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|wsdlLocation
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|String
name|getWsdlLocation
parameter_list|()
block|{
return|return
name|isSetWsdlLocation
argument_list|()
condition|?
name|wsdlLocation
else|:
literal|null
return|;
block|}
specifier|public
name|boolean
name|isSetWsdlLocation
parameter_list|()
block|{
return|return
operator|!
name|DEFAULT_WSDL_LOCATION
operator|.
name|equals
argument_list|(
name|wsdlLocation
argument_list|)
return|;
block|}
specifier|public
name|String
name|getFrontEnd
parameter_list|()
block|{
return|return
name|frontEnd
return|;
block|}
specifier|public
name|void
name|setFrontEnd
parameter_list|(
name|String
name|frontEnd
parameter_list|)
block|{
name|this
operator|.
name|frontEnd
operator|=
name|frontEnd
expr_stmt|;
block|}
specifier|public
name|String
name|getDataBinding
parameter_list|()
block|{
return|return
name|dataBinding
return|;
block|}
specifier|public
name|void
name|setDataBinding
parameter_list|(
name|String
name|dataBinding
parameter_list|)
block|{
name|this
operator|.
name|dataBinding
operator|=
name|dataBinding
expr_stmt|;
block|}
specifier|public
name|String
name|getWsdlVersion
parameter_list|()
block|{
return|return
name|wsdlVersion
return|;
block|}
specifier|public
name|void
name|setWsdlVersion
parameter_list|(
name|String
name|wsdlVersion
parameter_list|)
block|{
name|this
operator|.
name|wsdlVersion
operator|=
name|wsdlVersion
expr_stmt|;
block|}
specifier|public
name|String
name|getCatalog
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
specifier|public
name|void
name|setCatalog
parameter_list|(
name|String
name|catalog
parameter_list|)
block|{
name|this
operator|.
name|catalog
operator|=
name|catalog
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExtendedSoapHeaders
parameter_list|()
block|{
return|return
name|extendedSoapHeaders
return|;
block|}
specifier|public
name|void
name|setExtendedSoapHeaders
parameter_list|(
name|boolean
name|extendedSoapHeaders
parameter_list|)
block|{
name|this
operator|.
name|extendedSoapHeaders
operator|=
name|extendedSoapHeaders
expr_stmt|;
block|}
specifier|public
name|boolean
name|isValidateWsdl
parameter_list|()
block|{
return|return
name|validateWsdl
return|;
block|}
specifier|public
name|void
name|setValidateWsdl
parameter_list|(
name|boolean
name|validateWsdl
parameter_list|)
block|{
name|this
operator|.
name|validateWsdl
operator|=
name|validateWsdl
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getDefaultExcludesNamespace
parameter_list|()
block|{
return|return
name|defaultExcludesNamespace
return|;
block|}
specifier|public
name|void
name|setDefaultExcludesNamespace
parameter_list|(
name|Boolean
name|defaultExcludesNamespace
parameter_list|)
block|{
name|this
operator|.
name|defaultExcludesNamespace
operator|=
name|defaultExcludesNamespace
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getDefaultNamespacePackageMapping
parameter_list|()
block|{
return|return
name|defaultNamespacePackageMapping
return|;
block|}
specifier|public
name|void
name|setDefaultNamespacePackageMapping
parameter_list|(
name|Boolean
name|defaultNamespacePackageMapping
parameter_list|)
block|{
name|this
operator|.
name|defaultNamespacePackageMapping
operator|=
name|defaultNamespacePackageMapping
expr_stmt|;
block|}
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAutoNameResolution
parameter_list|()
block|{
return|return
name|autoNameResolution
return|;
block|}
specifier|public
name|void
name|setAutoNameResolution
parameter_list|(
name|boolean
name|autoNameResolution
parameter_list|)
block|{
name|this
operator|.
name|autoNameResolution
operator|=
name|autoNameResolution
expr_stmt|;
block|}
specifier|public
name|boolean
name|isNoAddressBinding
parameter_list|()
block|{
return|return
name|noAddressBinding
return|;
block|}
specifier|public
name|void
name|setNoAddressBinding
parameter_list|(
name|boolean
name|noAddressBinding
parameter_list|)
block|{
name|this
operator|.
name|noAddressBinding
operator|=
name|noAddressBinding
expr_stmt|;
block|}
specifier|public
name|void
name|copyOptions
parameter_list|(
name|Option
name|destination
parameter_list|)
block|{
name|destination
operator|.
name|setAutoNameResolution
argument_list|(
name|isAutoNameResolution
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setBindingFiles
argument_list|(
name|getBindingFiles
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setCatalog
argument_list|(
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setDataBinding
argument_list|(
name|getDataBinding
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setDefaultExcludesNamespace
argument_list|(
name|getDefaultExcludesNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setDefaultNamespacePackageMapping
argument_list|(
name|getDefaultNamespacePackageMapping
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setDeleteDirs
argument_list|(
name|getDeleteDirs
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setDependencies
argument_list|(
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setExtendedSoapHeaders
argument_list|(
name|isExtendedSoapHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setExtraargs
argument_list|(
name|getExtraargs
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setFrontEnd
argument_list|(
name|getFrontEnd
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setNamespaceExcludes
argument_list|(
name|namespaceExcludes
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setNoAddressBinding
argument_list|(
name|isNoAddressBinding
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setOutputDir
argument_list|(
name|getOutputDir
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setPackagenames
argument_list|(
name|getPackagenames
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setServiceName
argument_list|(
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setValidateWsdl
argument_list|(
name|isValidateWsdl
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSetWsdlLocation
argument_list|()
condition|)
block|{
name|destination
operator|.
name|setWsdlLocation
argument_list|(
name|getWsdlLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|destination
operator|.
name|setWsdlVersion
argument_list|(
name|getWsdlVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

