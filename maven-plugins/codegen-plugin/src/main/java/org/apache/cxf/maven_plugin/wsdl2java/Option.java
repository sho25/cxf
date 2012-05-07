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
operator|.
name|wsdl2java
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
name|HashSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
specifier|public
class|class
name|Option
block|{
specifier|static
specifier|final
name|String
name|DEFAULT_BINDING_FILE_PATH
init|=
literal|"src"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"main"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"resources"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"defaultBinding.xml"
decl_stmt|;
comment|/**      * As maven will set null for an empty parameter we need      * this horrid initial value to tell if it has been       * configured or not yet.      */
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_WSDL_LOCATION
init|=
literal|"DEFAULTWSDLLOCATION - WORKAROUND"
decl_stmt|;
comment|/**      *       */
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|packagenames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Extra arguments to pass to the command-line code generator. For compatibility as well as to      * specify any extra flags not addressed by other parameters      */
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
comment|/**      * Extra arguments to pass to the XJC compiler command-line code generator.      * For compatibility as well as to specify any extra flags not addressed by other parameters      */
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|xjcargs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|String
index|[]
name|asyncMethods
decl_stmt|;
specifier|protected
name|String
index|[]
name|bareMethods
decl_stmt|;
specifier|protected
name|String
index|[]
name|mimeMethods
decl_stmt|;
comment|/**      * Directory where generated java classes will be created. Defaults to plugin 'sourceRoot' parameter      */
specifier|protected
name|File
name|outputDir
decl_stmt|;
comment|/**      * Ignore the specified WSDL schema namespace when generating code.      * Also, optionally specifies the Java package name used by types described in the excluded       * namespace(s) using schema-namespace[=java-packagename]      */
name|List
argument_list|<
name|String
argument_list|>
name|namespaceExcludes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Enables or disables the loading of the default excludes namespace mapping. Default is true.      */
name|Boolean
name|defaultExcludesNamespace
decl_stmt|;
comment|/**      * Enables or disables the loading of the default namespace package name mapping. Default is true and       *<a href=""http://www.w3.org/2005/08/addressing">      * http://www.w3.org/2005/08/addressing=org.apache.cxf.ws.addressingnamespace</a>       * package mapping will be enabled.      */
name|Boolean
name|defaultNamespacePackageMapping
decl_stmt|;
comment|/**      * A set of dependent files used to detect that the generator must process WSDL, even       * if generator marker files are up to date.      */
name|File
name|dependencies
index|[]
decl_stmt|;
comment|/**      * Redundant directories to be deleted after code generation      */
name|File
name|redundantDirs
index|[]
decl_stmt|;
comment|/**      * Specifies JAXWS or JAXB binding files. Use spaces to separate multiple entries.      */
name|Set
argument_list|<
name|String
argument_list|>
name|bindingFiles
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Specifies the value of the @WebServiceClient annotation's wsdlLocation property.       */
name|String
name|wsdlLocation
init|=
name|DEFAULT_WSDL_LOCATION
decl_stmt|;
comment|/**      * Specifies that the wsdlurl contains a plain text, new line delimited,      * list of wsdlurls instead of the wsdl itself.      */
name|Boolean
name|wsdlList
decl_stmt|;
comment|/**      * Specifies the frontend. Default is JAXWS. Currently supports only JAXWS frontend.      */
name|String
name|frontEnd
decl_stmt|;
comment|/**      * Specifies the databinding. Default is JAXB. Currently supports only JAXB databinding.      */
name|String
name|dataBinding
decl_stmt|;
comment|/**      * Specifies the wsdl version .Default is WSDL1.1. Currently suppports only WSDL1.1 version.      */
name|String
name|wsdlVersion
decl_stmt|;
comment|/**      * Specify catalog file to map the imported wsdl/schema      */
name|String
name|catalog
decl_stmt|;
comment|/**      * Enables or disables processing of implicit SOAP headers (i.e. SOAP headers defined in the       * wsdl:binding but not wsdl:portType section.) Default is false.      */
name|Boolean
name|extendedSoapHeaders
decl_stmt|;
comment|/**      * Enables validating the WSDL before generating the code.       */
name|String
name|validateWsdl
decl_stmt|;
comment|/**      * Enables or disables generation of the type classes. Default value is false.      */
name|Boolean
name|noTypes
decl_stmt|;
comment|/**      * specify how to generate fault Exception's SUID, default is NONE      */
name|String
name|faultSerialVersionUID
decl_stmt|;
comment|/**      * The superclass to use for generated exceptions, default is java.lang.Exception      */
name|String
name|exceptionSuper
decl_stmt|;
comment|/**      * Uses @Generated annotation in all generated java classes if the flag is set to true.      */
name|Boolean
name|markGenerated
decl_stmt|;
comment|/**      * The WSDL service name to use for the generated code      */
name|String
name|serviceName
decl_stmt|;
comment|/**      * Automatically resolve naming conflicts without requiring the use of binding customizations      */
name|Boolean
name|autoNameResolution
decl_stmt|;
comment|/**      * Disable generation of service address binding in the generated Java classes      */
name|Boolean
name|noAddressBinding
decl_stmt|;
comment|/**      * Allow element references when determining if an operation can be unwrapped or not       */
name|Boolean
name|allowElementRefs
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
name|getXJCargs
parameter_list|()
block|{
return|return
name|xjcargs
return|;
block|}
specifier|public
name|void
name|setXJCargs
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
name|xjcargs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|xjcargs
operator|.
name|addAll
argument_list|(
name|ea
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setMimeMethods
parameter_list|(
name|String
index|[]
name|methods
parameter_list|)
block|{
name|mimeMethods
operator|=
name|methods
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getMimeMethods
parameter_list|()
block|{
return|return
name|mimeMethods
return|;
block|}
specifier|public
name|void
name|setAsyncMethods
parameter_list|(
name|String
index|[]
name|methods
parameter_list|)
block|{
name|asyncMethods
operator|=
name|methods
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getAsyncMethods
parameter_list|()
block|{
return|return
name|asyncMethods
return|;
block|}
specifier|public
name|void
name|setBareMethods
parameter_list|(
name|String
index|[]
name|methods
parameter_list|)
block|{
name|bareMethods
operator|=
name|methods
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getBareMethods
parameter_list|()
block|{
return|return
name|bareMethods
return|;
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
name|Set
argument_list|<
name|String
argument_list|>
name|files
parameter_list|)
block|{
name|bindingFiles
operator|=
name|files
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
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
name|bindingFiles
operator|.
name|add
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addDefaultBindingFileIfExists
parameter_list|(
name|File
name|baseDir
parameter_list|)
block|{
name|File
name|defaultBindingFile
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
name|DEFAULT_BINDING_FILE_PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultBindingFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|addBindingFile
argument_list|(
name|defaultBindingFile
argument_list|)
expr_stmt|;
block|}
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
name|boolean
name|isWsdlList
parameter_list|()
block|{
return|return
name|wsdlList
operator|==
literal|null
condition|?
literal|false
else|:
name|wsdlList
return|;
block|}
specifier|public
name|void
name|setWsdlList
parameter_list|(
name|boolean
name|wsdlList
parameter_list|)
block|{
name|this
operator|.
name|wsdlList
operator|=
name|wsdlList
expr_stmt|;
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
operator|==
literal|null
condition|?
literal|false
else|:
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
name|String
name|getValidateWsdl
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
name|String
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
name|void
name|setValidate
parameter_list|(
name|String
name|v
parameter_list|)
block|{
name|this
operator|.
name|validateWsdl
operator|=
name|v
expr_stmt|;
block|}
specifier|public
name|boolean
name|isNoTypes
parameter_list|()
block|{
return|return
name|noTypes
operator|==
literal|null
condition|?
literal|false
else|:
name|noTypes
return|;
block|}
specifier|public
name|void
name|setNoTypes
parameter_list|(
name|boolean
name|noTypes
parameter_list|)
block|{
name|this
operator|.
name|noTypes
operator|=
name|noTypes
expr_stmt|;
block|}
specifier|public
name|String
name|getFaultSerialVersionUID
parameter_list|()
block|{
return|return
name|faultSerialVersionUID
return|;
block|}
specifier|public
name|void
name|setFaultSerialVersionUID
parameter_list|(
name|String
name|faultSerialVersionUID
parameter_list|)
block|{
name|this
operator|.
name|faultSerialVersionUID
operator|=
name|faultSerialVersionUID
expr_stmt|;
block|}
specifier|public
name|String
name|getExceptionSuper
parameter_list|()
block|{
return|return
name|exceptionSuper
return|;
block|}
specifier|public
name|void
name|setExceptionSuper
parameter_list|(
name|String
name|exceptionSuper
parameter_list|)
block|{
name|this
operator|.
name|exceptionSuper
operator|=
name|exceptionSuper
expr_stmt|;
block|}
specifier|public
name|Boolean
name|isMarkGenerated
parameter_list|()
block|{
return|return
name|markGenerated
return|;
block|}
specifier|public
name|void
name|setMarkGenerated
parameter_list|(
name|Boolean
name|markGenerated
parameter_list|)
block|{
name|this
operator|.
name|markGenerated
operator|=
name|markGenerated
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
operator|==
literal|null
condition|?
literal|false
else|:
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
operator|==
literal|null
condition|?
literal|false
else|:
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
name|boolean
name|isAllowElementRefs
parameter_list|()
block|{
return|return
name|allowElementRefs
operator|==
literal|null
condition|?
literal|false
else|:
name|allowElementRefs
return|;
block|}
specifier|public
name|void
name|setAllowElementRefs
parameter_list|(
name|boolean
name|allowElementRefs
parameter_list|)
block|{
name|this
operator|.
name|allowElementRefs
operator|=
name|allowElementRefs
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
name|setXJCargs
argument_list|(
name|getXJCargs
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
name|getValidateWsdl
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setNoTypes
argument_list|(
name|isNoTypes
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setFaultSerialVersionUID
argument_list|(
name|getFaultSerialVersionUID
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setMarkGenerated
argument_list|(
name|isMarkGenerated
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setAllowElementRefs
argument_list|(
name|isAllowElementRefs
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
name|destination
operator|.
name|setMimeMethods
argument_list|(
name|getMimeMethods
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setAsyncMethods
argument_list|(
name|getAsyncMethods
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setBareMethods
argument_list|(
name|getBareMethods
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setExceptionSuper
argument_list|(
name|getExceptionSuper
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|setIfNull
parameter_list|(
name|T
name|dest
parameter_list|,
name|T
name|source
parameter_list|)
block|{
if|if
condition|(
name|dest
operator|==
literal|null
condition|)
block|{
name|dest
operator|=
name|source
expr_stmt|;
block|}
return|return
name|dest
return|;
block|}
specifier|public
name|void
name|merge
parameter_list|(
name|Option
name|defaultOptions
parameter_list|)
block|{
name|wsdlList
operator|=
name|setIfNull
argument_list|(
name|wsdlList
argument_list|,
name|defaultOptions
operator|.
name|wsdlList
argument_list|)
expr_stmt|;
name|exceptionSuper
operator|=
name|setIfNull
argument_list|(
name|exceptionSuper
argument_list|,
name|defaultOptions
operator|.
name|exceptionSuper
argument_list|)
expr_stmt|;
name|extendedSoapHeaders
operator|=
name|setIfNull
argument_list|(
name|extendedSoapHeaders
argument_list|,
name|defaultOptions
operator|.
name|extendedSoapHeaders
argument_list|)
expr_stmt|;
name|noTypes
operator|=
name|setIfNull
argument_list|(
name|noTypes
argument_list|,
name|defaultOptions
operator|.
name|noTypes
argument_list|)
expr_stmt|;
name|validateWsdl
operator|=
name|setIfNull
argument_list|(
name|validateWsdl
argument_list|,
name|defaultOptions
operator|.
name|validateWsdl
argument_list|)
expr_stmt|;
name|faultSerialVersionUID
operator|=
name|setIfNull
argument_list|(
name|faultSerialVersionUID
argument_list|,
name|defaultOptions
operator|.
name|faultSerialVersionUID
argument_list|)
expr_stmt|;
name|markGenerated
operator|=
name|setIfNull
argument_list|(
name|markGenerated
argument_list|,
name|defaultOptions
operator|.
name|markGenerated
argument_list|)
expr_stmt|;
name|autoNameResolution
operator|=
name|setIfNull
argument_list|(
name|autoNameResolution
argument_list|,
name|defaultOptions
operator|.
name|autoNameResolution
argument_list|)
expr_stmt|;
name|noAddressBinding
operator|=
name|setIfNull
argument_list|(
name|noAddressBinding
argument_list|,
name|defaultOptions
operator|.
name|noAddressBinding
argument_list|)
expr_stmt|;
name|allowElementRefs
operator|=
name|setIfNull
argument_list|(
name|allowElementRefs
argument_list|,
name|defaultOptions
operator|.
name|allowElementRefs
argument_list|)
expr_stmt|;
name|defaultExcludesNamespace
operator|=
name|setIfNull
argument_list|(
name|defaultExcludesNamespace
argument_list|,
name|defaultOptions
operator|.
name|defaultExcludesNamespace
argument_list|)
expr_stmt|;
name|defaultNamespacePackageMapping
operator|=
name|setIfNull
argument_list|(
name|defaultNamespacePackageMapping
argument_list|,
name|defaultOptions
operator|.
name|defaultNamespacePackageMapping
argument_list|)
expr_stmt|;
name|frontEnd
operator|=
name|setIfNull
argument_list|(
name|frontEnd
argument_list|,
name|defaultOptions
operator|.
name|frontEnd
argument_list|)
expr_stmt|;
name|dataBinding
operator|=
name|setIfNull
argument_list|(
name|dataBinding
argument_list|,
name|defaultOptions
operator|.
name|dataBinding
argument_list|)
expr_stmt|;
name|wsdlVersion
operator|=
name|setIfNull
argument_list|(
name|wsdlVersion
argument_list|,
name|defaultOptions
operator|.
name|wsdlVersion
argument_list|)
expr_stmt|;
name|catalog
operator|=
name|setIfNull
argument_list|(
name|catalog
argument_list|,
name|defaultOptions
operator|.
name|catalog
argument_list|)
expr_stmt|;
name|serviceName
operator|=
name|setIfNull
argument_list|(
name|serviceName
argument_list|,
name|defaultOptions
operator|.
name|serviceName
argument_list|)
expr_stmt|;
name|outputDir
operator|=
name|setIfNull
argument_list|(
name|outputDir
argument_list|,
name|defaultOptions
operator|.
name|outputDir
argument_list|)
expr_stmt|;
name|extraargs
operator|.
name|addAll
argument_list|(
name|defaultOptions
operator|.
name|extraargs
argument_list|)
expr_stmt|;
name|xjcargs
operator|.
name|addAll
argument_list|(
name|defaultOptions
operator|.
name|xjcargs
argument_list|)
expr_stmt|;
name|bindingFiles
operator|.
name|addAll
argument_list|(
name|defaultOptions
operator|.
name|getBindingFiles
argument_list|()
argument_list|)
expr_stmt|;
name|dependencies
operator|=
name|mergeList
argument_list|(
name|dependencies
argument_list|,
name|defaultOptions
operator|.
name|dependencies
argument_list|,
name|File
operator|.
name|class
argument_list|)
expr_stmt|;
name|redundantDirs
operator|=
name|mergeList
argument_list|(
name|redundantDirs
argument_list|,
name|defaultOptions
operator|.
name|redundantDirs
argument_list|,
name|File
operator|.
name|class
argument_list|)
expr_stmt|;
name|packagenames
operator|.
name|addAll
argument_list|(
name|defaultOptions
operator|.
name|packagenames
argument_list|)
expr_stmt|;
name|namespaceExcludes
operator|.
name|addAll
argument_list|(
name|defaultOptions
operator|.
name|namespaceExcludes
argument_list|)
expr_stmt|;
name|bareMethods
operator|=
name|mergeList
argument_list|(
name|bareMethods
argument_list|,
name|defaultOptions
operator|.
name|bareMethods
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|asyncMethods
operator|=
name|mergeList
argument_list|(
name|asyncMethods
argument_list|,
name|defaultOptions
operator|.
name|asyncMethods
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|mimeMethods
operator|=
name|mergeList
argument_list|(
name|mimeMethods
argument_list|,
name|defaultOptions
operator|.
name|mimeMethods
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isSetWsdlLocation
argument_list|()
operator|&&
name|defaultOptions
operator|.
name|isSetWsdlLocation
argument_list|()
condition|)
block|{
name|wsdlLocation
operator|=
name|defaultOptions
operator|.
name|getWsdlLocation
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|mergeList
parameter_list|(
name|T
index|[]
name|l1
parameter_list|,
name|T
index|[]
name|l2
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|l1
operator|==
literal|null
condition|)
block|{
return|return
name|l2
return|;
block|}
elseif|else
if|if
condition|(
name|l2
operator|==
literal|null
condition|)
block|{
return|return
name|l1
return|;
block|}
name|int
name|len
init|=
name|l1
operator|.
name|length
operator|+
name|l2
operator|.
name|length
decl_stmt|;
name|T
name|ret
index|[]
init|=
operator|(
name|T
index|[]
operator|)
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
operator|.
name|newInstance
argument_list|(
name|cls
argument_list|,
name|len
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|l1
argument_list|,
literal|0
argument_list|,
name|ret
argument_list|,
literal|0
argument_list|,
name|l1
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|l2
argument_list|,
literal|0
argument_list|,
name|ret
argument_list|,
name|l1
operator|.
name|length
argument_list|,
name|l2
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

