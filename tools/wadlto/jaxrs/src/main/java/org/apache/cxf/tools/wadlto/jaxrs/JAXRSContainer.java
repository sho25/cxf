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
name|tools
operator|.
name|wadlto
operator|.
name|jaxrs
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|URIParserUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|AbstractCXFToolContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ClassUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ToolException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|toolspec
operator|.
name|ToolSpec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|toolspec
operator|.
name|parser
operator|.
name|BadUsageException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|util
operator|.
name|ClassCollector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wadlto
operator|.
name|WadlToolConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wadlto
operator|.
name|jaxb
operator|.
name|CustomizationParser
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSContainer
extends|extends
name|AbstractCXFToolContainer
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|DEFAULT_JAVA_TYPE_MAP
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TOOL_NAME
init|=
literal|"wadl2java"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EPR_TYPE_KEY
init|=
literal|"org.w3._2005._08.addressing.EndpointReference"
decl_stmt|;
static|static
block|{
name|DEFAULT_JAVA_TYPE_MAP
operator|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|EPR_TYPE_KEY
argument_list|,
literal|"javax.xml.ws.wsaddressing.W3CEndpointReference"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXRSContainer
parameter_list|(
name|ToolSpec
name|toolspec
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|TOOL_NAME
argument_list|,
name|toolspec
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|ToolException
block|{
if|if
condition|(
name|hasInfoOption
argument_list|()
condition|)
block|{
return|return;
block|}
name|buildToolContext
argument_list|()
expr_stmt|;
name|processWadl
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|boolean
name|exitOnFinish
parameter_list|)
throws|throws
name|ToolException
block|{
try|try
block|{
if|if
condition|(
name|getArgument
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|super
operator|.
name|execute
argument_list|(
name|exitOnFinish
argument_list|)
expr_stmt|;
block|}
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ToolException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BadUsageException
condition|)
block|{
name|printUsageException
argument_list|(
name|TOOL_NAME
argument_list|,
operator|(
name|BadUsageException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|buildToolContext
parameter_list|()
block|{
name|getContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addParameters
argument_list|(
name|getParametersMap
argument_list|(
name|getArrayKeys
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
operator|==
literal|null
condition|)
block|{
name|context
operator|.
name|put
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
literal|"."
argument_list|)
expr_stmt|;
block|}
name|setPackageAndNamespaces
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getArrayKeys
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_BINDING
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_SCHEMA_PACKAGENAME
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_SCHEMA_TYPE_MAP
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_MEDIA_TYPE_MAP
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_XJC_ARGS
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
specifier|private
name|void
name|processWadl
parameter_list|()
block|{
name|File
name|outDir
init|=
operator|new
name|File
argument_list|(
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|wadlURL
init|=
name|getAbsoluteWadlURL
argument_list|()
decl_stmt|;
name|String
name|wadl
init|=
name|readWadl
argument_list|(
name|wadlURL
argument_list|)
decl_stmt|;
name|SourceGenerator
name|sg
init|=
operator|new
name|SourceGenerator
argument_list|()
decl_stmt|;
name|sg
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|generateImpl
init|=
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_IMPL
argument_list|)
decl_stmt|;
name|sg
operator|.
name|setGenerateImplementation
argument_list|(
name|generateImpl
argument_list|)
expr_stmt|;
if|if
condition|(
name|generateImpl
condition|)
block|{
name|sg
operator|.
name|setGenerateInterfaces
argument_list|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_INTERFACE
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sg
operator|.
name|setPackageName
argument_list|(
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_PACKAGENAME
argument_list|)
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setResourceName
argument_list|(
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_RESOURCENAME
argument_list|)
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setEncoding
argument_list|(
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_ENCODING
argument_list|)
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setNamePassword
argument_list|(
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_NAME_PASSWORD
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|wadlNs
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_WADL_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|wadlNs
operator|!=
literal|null
condition|)
block|{
name|sg
operator|.
name|setWadlNamespace
argument_list|(
name|wadlNs
argument_list|)
expr_stmt|;
block|}
name|sg
operator|.
name|setSupportMultipleXmlReps
argument_list|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_MULTIPLE_XML_REPS
argument_list|)
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setSupportBeanValidation
argument_list|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_BEAN_VALIDATION
argument_list|)
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setCreateJavaDocs
argument_list|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_CREATE_JAVA_DOCS
argument_list|)
argument_list|)
expr_stmt|;
comment|// set the base path
name|sg
operator|.
name|setWadlPath
argument_list|(
name|wadlURL
argument_list|)
expr_stmt|;
name|CustomizationParser
name|parser
init|=
operator|new
name|CustomizationParser
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InputSource
argument_list|>
name|bindingFiles
init|=
name|parser
operator|.
name|getJaxbBindings
argument_list|()
decl_stmt|;
name|sg
operator|.
name|setBindingFiles
argument_list|(
name|bindingFiles
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setCompilerArgs
argument_list|(
name|parser
operator|.
name|getCompilerArgs
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InputSource
argument_list|>
name|schemaPackageFiles
init|=
name|parser
operator|.
name|getSchemaPackageFiles
argument_list|()
decl_stmt|;
name|sg
operator|.
name|setSchemaPackageFiles
argument_list|(
name|schemaPackageFiles
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setSchemaPackageMap
argument_list|(
name|context
operator|.
name|getNamespacePackageMap
argument_list|()
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setJavaTypeMap
argument_list|(
name|DEFAULT_JAVA_TYPE_MAP
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setSchemaTypeMap
argument_list|(
name|getSchemaTypeMap
argument_list|()
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setMediaTypeMap
argument_list|(
name|getMediaTypeMap
argument_list|()
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setSuspendedAsyncMethods
argument_list|(
name|getSuspendedAsyncMethods
argument_list|()
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setResponseMethods
argument_list|(
name|getResponseMethods
argument_list|()
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setGenerateEnums
argument_list|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_GENERATE_ENUMS
argument_list|)
argument_list|)
expr_stmt|;
name|sg
operator|.
name|setValidateWadl
argument_list|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_VALIDATE_WADL
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|inheritResourceParams
init|=
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_INHERIT_PARAMS
argument_list|)
decl_stmt|;
name|sg
operator|.
name|setInheritResourceParams
argument_list|(
name|inheritResourceParams
argument_list|)
expr_stmt|;
if|if
condition|(
name|inheritResourceParams
condition|)
block|{
name|sg
operator|.
name|setInheritResourceParamsFirst
argument_list|(
name|isInheritResourceParamsFirst
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sg
operator|.
name|setSkipSchemaGeneration
argument_list|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_NO_TYPES
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|noVoidForEmptyResponses
init|=
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_NO_VOID_FOR_EMPTY_RESPONSES
argument_list|)
decl_stmt|;
if|if
condition|(
name|noVoidForEmptyResponses
condition|)
block|{
name|sg
operator|.
name|setUseVoidForEmptyResponses
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|sg
operator|.
name|setGenerateResponseIfHeadersSet
argument_list|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_GENERATE_RESPONSE_IF_HEADERS_SET
argument_list|)
argument_list|)
expr_stmt|;
comment|// generate
name|String
name|codeType
init|=
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_TYPES
argument_list|)
condition|?
name|SourceGenerator
operator|.
name|CODE_TYPE_GRAMMAR
else|:
name|SourceGenerator
operator|.
name|CODE_TYPE_PROXY
decl_stmt|;
name|sg
operator|.
name|generateSource
argument_list|(
name|wadl
argument_list|,
name|outDir
argument_list|,
name|codeType
argument_list|)
expr_stmt|;
comment|// compile
if|if
condition|(
name|context
operator|.
name|optionSet
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_COMPILE
argument_list|)
condition|)
block|{
name|ClassCollector
name|collector
init|=
name|createClassCollector
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|generatedServiceClasses
init|=
name|sg
operator|.
name|getGeneratedServiceClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|className
range|:
name|generatedServiceClasses
control|)
block|{
name|int
name|index
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|collector
operator|.
name|addServiceClassName
argument_list|(
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|,
name|className
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|generatedTypeClasses
init|=
name|sg
operator|.
name|getGeneratedTypeClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|className
range|:
name|generatedTypeClasses
control|)
block|{
name|int
name|index
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|collector
operator|.
name|addTypesClassName
argument_list|(
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|,
name|className
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|put
argument_list|(
name|ClassCollector
operator|.
name|class
argument_list|,
name|collector
argument_list|)
expr_stmt|;
operator|new
name|ClassUtils
argument_list|()
operator|.
name|compile
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|readWadl
parameter_list|(
name|String
name|wadlURI
parameter_list|)
block|{
try|try
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|wadlURI
argument_list|)
decl_stmt|;
name|Reader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
return|return
name|IOUtils
operator|.
name|toString
argument_list|(
name|reader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|getAbsoluteWadlURL
parameter_list|()
block|{
name|String
name|wadlURL
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_WADLURL
argument_list|)
decl_stmt|;
name|String
name|absoluteWadlURL
init|=
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|wadlURL
argument_list|)
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_WADLURL
argument_list|,
name|absoluteWadlURL
argument_list|)
expr_stmt|;
return|return
name|absoluteWadlURL
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getSuspendedAsyncMethods
parameter_list|()
block|{
return|return
name|parseMethodList
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_SUSPENDED_ASYNC
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getResponseMethods
parameter_list|()
block|{
return|return
name|parseMethodList
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_GENERATE_RESPONSE_FOR_METHODS
argument_list|)
return|;
block|}
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|parseMethodList
parameter_list|(
name|String
name|paramName
parameter_list|)
block|{
name|Object
name|value
init|=
name|context
operator|.
name|get
argument_list|(
name|paramName
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|methods
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|String
index|[]
name|values
init|=
name|value
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|values
control|)
block|{
name|String
name|actual
init|=
name|s
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|actual
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|actual
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|methods
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|methods
operator|.
name|add
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
block|}
return|return
name|methods
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|isInheritResourceParamsFirst
parameter_list|()
block|{
name|Object
name|value
init|=
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_INHERIT_PARAMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|"first"
operator|.
name|equals
argument_list|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
comment|//TODO: this belongs to JAXB Databinding, should we just reuse
comment|// org.apache.cxf.tools.wsdlto.databinding.jaxb ?
specifier|private
name|void
name|setPackageAndNamespaces
parameter_list|()
block|{
name|String
index|[]
name|schemaPackageNamespaces
init|=
operator|new
name|String
index|[]
block|{}
decl_stmt|;
name|Object
name|value
init|=
name|context
operator|.
name|get
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_SCHEMA_PACKAGENAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|schemaPackageNamespaces
operator|=
name|value
operator|instanceof
name|String
condition|?
operator|new
name|String
index|[]
block|{
operator|(
name|String
operator|)
name|value
block|}
else|:
operator|(
name|String
index|[]
operator|)
name|value
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|schemaPackageNamespaces
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|pos
init|=
name|schemaPackageNamespaces
index|[
name|i
index|]
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
name|String
name|packagename
init|=
name|schemaPackageNamespaces
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|ns
init|=
name|schemaPackageNamespaces
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|packagename
operator|=
name|schemaPackageNamespaces
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addNamespacePackageMap
argument_list|(
name|ns
argument_list|,
name|packagename
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// this is the default schema package name
comment|// if CFG_PACKAGENAME is set then it's only used for JAX-RS resource
comment|// classes
name|context
operator|.
name|put
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_SCHEMA_PACKAGENAME
argument_list|,
name|packagename
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getSchemaTypeMap
parameter_list|()
block|{
return|return
name|getMap
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_SCHEMA_TYPE_MAP
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMediaTypeMap
parameter_list|()
block|{
return|return
name|getMap
argument_list|(
name|WadlToolConstants
operator|.
name|CFG_MEDIA_TYPE_MAP
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMap
parameter_list|(
name|String
name|parameterName
parameter_list|)
block|{
name|String
index|[]
name|typeToClasses
init|=
operator|new
name|String
index|[]
block|{}
decl_stmt|;
name|Object
name|value
init|=
name|context
operator|.
name|get
argument_list|(
name|parameterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|typeToClasses
operator|=
name|value
operator|instanceof
name|String
condition|?
operator|new
name|String
index|[]
block|{
operator|(
name|String
operator|)
name|value
block|}
else|:
operator|(
name|String
index|[]
operator|)
name|value
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|typeToClasses
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|pos
init|=
name|typeToClasses
index|[
name|i
index|]
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|type
init|=
name|typeToClasses
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|contains
argument_list|(
literal|"%3D"
argument_list|)
condition|)
block|{
name|type
operator|=
name|type
operator|.
name|replace
argument_list|(
literal|"%3D"
argument_list|,
literal|"="
argument_list|)
expr_stmt|;
block|}
name|String
name|clsName
init|=
name|typeToClasses
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
name|typeMap
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|clsName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|typeMap
return|;
block|}
block|}
end_class

end_unit

