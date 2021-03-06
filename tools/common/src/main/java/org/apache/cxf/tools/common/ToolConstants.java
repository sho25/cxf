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
name|common
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ToolConstants
block|{
comment|//public static final String TOOLSPECS_BASE = "/org/apache/cxf/tools/common/toolspec/toolspecs/";
specifier|public
specifier|static
specifier|final
name|String
name|TOOLSPECS_BASE
init|=
literal|"/org/apache/cxf/tools/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCHEMA_URI
init|=
literal|"http://www.w3.org/2001/XMLSchema"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XML_NAMESPACE_URI
init|=
literal|"http://www.w3.org/XML/1998/namespace"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSDL_NAMESPACE_URI
init|=
literal|"http://schemas.xmlsoap.org/wsdl/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NAMESPACE_URI
init|=
literal|"http://www.w3.org/2005/08/addressing"
decl_stmt|;
comment|/**      * Tools permit caller to pass in additional bean definitions.      */
specifier|public
specifier|static
specifier|final
name|String
name|CFG_BEAN_CONFIG
init|=
literal|"beans"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_TEMP_DIR
init|=
literal|"gen_tmp"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_OUTPUTDIR
init|=
literal|"outputdir"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_OUTPUTFILE
init|=
literal|"outputfile"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WSDLURL
init|=
literal|"wsdlurl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WSDLLOCATION
init|=
literal|"wsdlLocation"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_ENCODING
init|=
literal|"encoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WSDLLIST
init|=
literal|"wsdlList"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NAMESPACE
init|=
literal|"namespace"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_VERBOSE
init|=
literal|"verbose"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_PORT
init|=
literal|"port"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_BINDING
init|=
literal|"binding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_AUTORESOLVE
init|=
literal|"autoNameResolution"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WEBSERVICE
init|=
literal|"webservice"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SERVER
init|=
literal|"server"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CLIENT
init|=
literal|"client"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CLIENT_JAR
init|=
literal|"clientjar"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_ALL
init|=
literal|"all"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_IMPL
init|=
literal|"impl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_PACKAGENAME
init|=
literal|"packagename"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_JSPACKAGEPREFIX
init|=
literal|"jspackageprefix"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NINCLUDE
init|=
literal|"ninclude"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NEXCLUDE
init|=
literal|"nexclude"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CMD_ARG
init|=
literal|"args"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_INSTALL_DIR
init|=
literal|"install.dir"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_PLATFORM_VERSION
init|=
literal|"platform.version"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_COMPILE
init|=
literal|"compile"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CLASSDIR
init|=
literal|"classdir"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_EXTRA_SOAPHEADER
init|=
literal|"exsoapheader"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_DEFAULT_NS
init|=
literal|"defaultns"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_DEFAULT_EX
init|=
literal|"defaultex"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NO_TYPES
init|=
literal|"notypes"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_XJC_ARGS
init|=
literal|"xjc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CATALOG
init|=
literal|"catalog"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_BAREMETHODS
init|=
literal|"bareMethods"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_ASYNCMETHODS
init|=
literal|"asyncMethods"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_MIMEMETHODS
init|=
literal|"mimeMethods"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_DEFAULT_VALUES
init|=
literal|"defaultValues"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_JAVASCRIPT_UTILS
init|=
literal|"javascriptUtils"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_VALIDATE_WSDL
init|=
literal|"validate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CREATE_XSD_IMPORTS
init|=
literal|"createxsdimports"
decl_stmt|;
comment|/**      * Front-end selection command-line option to java2ws.      */
specifier|public
specifier|static
specifier|final
name|String
name|CFG_FRONTEND
init|=
literal|"frontend"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_DATABINDING
init|=
literal|"databinding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ADDRESS
init|=
literal|"http://localhost:9090"
decl_stmt|;
comment|// WSDL2Java Constants
specifier|public
specifier|static
specifier|final
name|String
name|CFG_TYPES
init|=
literal|"types"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_INTERFACE
init|=
literal|"interface"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NIGNOREEXCLUDE
init|=
literal|"nignoreexclude"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_ANT
init|=
literal|"ant"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_LIB_REF
init|=
literal|"library.references"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_ANT_PROP
init|=
literal|"ant.prop"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NO_ADDRESS_BINDING
init|=
literal|"noAddressBinding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_ALLOW_ELEMENT_REFS
init|=
literal|"allowElementReferences"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_RESERVE_NAME
init|=
literal|"reserveClass"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_FAULT_SERIAL_VERSION_UID
init|=
literal|"faultSerialVersionUID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_EXCEPTION_SUPER
init|=
literal|"exceptionSuper"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SEI_SUPER
init|=
literal|"seiSuper"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_MARK_GENERATED
init|=
literal|"markGenerated"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SUPPRESS_GENERATED_DATE
init|=
literal|"suppressGeneratedDate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_MARK_GENERATED_OPTION
init|=
literal|"mark-generated"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SUPPRESS_GENERATED_DATE_OPTION
init|=
literal|"suppress-generated-date"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_MAX_EXTENSION_STACK_DEPTH
init|=
literal|"maxExtensionStackDepth"
decl_stmt|;
comment|//Internal Flag to generate
specifier|public
specifier|static
specifier|final
name|String
name|CFG_IMPL_CLASS
init|=
literal|"implClass"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_CLIENT
init|=
literal|"genClient"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_SERVER
init|=
literal|"genServer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_IMPL
init|=
literal|"genImpl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_TYPES
init|=
literal|"genTypes"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_SEI
init|=
literal|"genSEI"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_ANT
init|=
literal|"genAnt"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_SERVICE
init|=
literal|"genService"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_OVERWRITE
init|=
literal|"overwrite"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_FAULT
init|=
literal|"genFault"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GEN_NEW_ONLY
init|=
literal|"newonly"
decl_stmt|;
comment|// Java2WSDL Constants
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CLASSPATH
init|=
literal|"classpath"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_TNS
init|=
literal|"tns"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SERVICENAME
init|=
literal|"servicename"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SCHEMANS
init|=
literal|"schemans"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_USETYPES
init|=
literal|"usetypes"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CLASSNAME
init|=
literal|"classname"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_PORTTYPE
init|=
literal|"porttype"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SOURCEDIR
init|=
literal|"sourcedir"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WSDL
init|=
literal|"wsdl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WRAPPERBEAN
init|=
literal|"wrapperbean"
decl_stmt|;
comment|// WSDL2Service Constants
specifier|public
specifier|static
specifier|final
name|String
name|CFG_ADDRESS
init|=
literal|"address"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_TRANSPORT
init|=
literal|"transport"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SERVICE
init|=
literal|"service"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_BINDING_ATTR
init|=
literal|"attrbinding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SOAP12
init|=
literal|"soap12"
decl_stmt|;
comment|// WSDL2Soap Constants
specifier|public
specifier|static
specifier|final
name|String
name|CFG_STYLE
init|=
literal|"style"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_USE
init|=
literal|"use"
decl_stmt|;
comment|// XSD2WSDL Constants
specifier|public
specifier|static
specifier|final
name|String
name|CFG_XSDURL
init|=
literal|"xsdurl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NAME
init|=
literal|"name"
decl_stmt|;
comment|// WsdlValidator
specifier|public
specifier|static
specifier|final
name|String
name|CFG_DEEP
init|=
literal|"deep"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SCHEMA_DIR
init|=
literal|"schemaDir"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SCHEMA_URL
init|=
literal|"schemaURL"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CXF_SCHEMA_DIR
init|=
literal|"cxf_schema_dir"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CXF_SCHEMAS_DIR_INJAR
init|=
literal|"schemas/wsdl/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SUPPRESS_WARNINGS
init|=
literal|"suppressWarnings"
decl_stmt|;
comment|// WSDL2Java Processor Constants
specifier|public
specifier|static
specifier|final
name|String
name|SEI_GENERATOR
init|=
literal|"sei.generator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FAULT_GENERATOR
init|=
literal|"fault.generator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_GENERATOR
init|=
literal|"type.generator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IMPL_GENERATOR
init|=
literal|"impl.generator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SVR_GENERATOR
init|=
literal|"svr.generator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLT_GENERATOR
init|=
literal|"clt.generator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_GENERATOR
init|=
literal|"service.generator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ANT_GENERATOR
init|=
literal|"ant.generator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HANDLER_GENERATOR
init|=
literal|"handler.generator"
decl_stmt|;
comment|// Binding namespace
specifier|public
specifier|static
specifier|final
name|String
name|NS_JAXWS_BINDINGS
init|=
literal|"http://java.sun.com/xml/ns/jaxws"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NS_JAXB_BINDINGS
init|=
literal|"http://java.sun.com/xml/ns/jaxb"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|JAXWS_BINDINGS
init|=
operator|new
name|QName
argument_list|(
name|NS_JAXWS_BINDINGS
argument_list|,
literal|"bindings"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|JAXB_BINDINGS
init|=
operator|new
name|QName
argument_list|(
name|NS_JAXB_BINDINGS
argument_list|,
literal|"bindings"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SCHEMA
init|=
operator|new
name|QName
argument_list|(
name|SCHEMA_URI
argument_list|,
literal|"schema"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAXWS_BINDINGS_WSDL_LOCATION
init|=
literal|"wsdlLocation"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAXWS_BINDING_NODE
init|=
literal|"node"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAXWS_BINDING_VERSION
init|=
literal|"version"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ASYNC_METHOD_SUFFIX
init|=
literal|"Async"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HANDLER_CHAINS_URI
init|=
literal|"http://java.sun.com/xml/ns/javaee"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HANDLER_CHAIN
init|=
literal|"handler-chain"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HANDLER_CHAINS
init|=
literal|"handler-chains"
decl_stmt|;
comment|//public static final String RAW_JAXB_MODEL = "rawjaxbmodel";
comment|// JMS address
specifier|public
specifier|static
specifier|final
name|String
name|NS_JMS_ADDRESS
init|=
literal|"http://cxf.apache.org/transports/jms"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|JMS_ADDRESS
init|=
operator|new
name|QName
argument_list|(
name|NS_JMS_ADDRESS
argument_list|,
literal|"address"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_ADDR_DEST_STYLE
init|=
literal|"destinationStyle"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_ADDR_JNDI_URL
init|=
literal|"jndiProviderURL"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_ADDR_JNDI_FAC
init|=
literal|"jndiConnectionFactoryName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_ADDR_JNDI_DEST
init|=
literal|"jndiDestinationName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_ADDR_MSG_TYPE
init|=
literal|"messageType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_ADDR_INIT_CTX
init|=
literal|"initialContextFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_ADDR_SUBSCRIBER_NAME
init|=
literal|"durableSubscriberName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_ADDR_MSGID_TO_CORRID
init|=
literal|"useMessageIDAsCorrelationID"
decl_stmt|;
comment|// XML Binding
specifier|public
specifier|static
specifier|final
name|String
name|XMLBINDING_ROOTNODE
init|=
literal|"rootNode"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XMLBINDING_HTTP_LOCATION
init|=
literal|"location"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NS_XML_FORMAT
init|=
literal|"http://cxf.apache.org/bindings/xformat"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XML_FORMAT_PREFIX
init|=
literal|"xformat"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NS_XML_HTTP
init|=
literal|"http://schemas.xmlsoap.org/wsdl/http/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XML_HTTP_PREFIX
init|=
literal|"http"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|XML_HTTP_ADDRESS
init|=
operator|new
name|QName
argument_list|(
name|NS_XML_HTTP
argument_list|,
literal|"address"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|XML_FORMAT
init|=
operator|new
name|QName
argument_list|(
name|NS_XML_FORMAT
argument_list|,
literal|"body"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|XML_BINDING_FORMAT
init|=
operator|new
name|QName
argument_list|(
name|NS_XML_FORMAT
argument_list|,
literal|"binding"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XML_SCHEMA_COLLECTION
init|=
literal|"xmlSchemaCollection"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORTTYPE_MAP
init|=
literal|"portTypeMap"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCHEMA_TARGET_NAMESPACES
init|=
literal|"schemaTargetNameSpaces"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSDL_DEFINITION
init|=
literal|"wsdlDefinition"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IMPORTED_DEFINITION
init|=
literal|"importedDefinition"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IMPORTED_PORTTYPE
init|=
literal|"importedPortType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IMPORTED_SERVICE
init|=
literal|"importedService"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BINDING_GENERATOR
init|=
literal|"BindingGenerator"
decl_stmt|;
comment|// Tools framework
specifier|public
specifier|static
specifier|final
name|String
name|FRONTEND_PLUGIN
init|=
literal|"frontend"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATABINDING_PLUGIN
init|=
literal|"databinding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RUNTIME_DATABINDING_CLASS
init|=
literal|"databinding-class"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMPILER
init|=
literal|"compiler"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WSDL_VERSION
init|=
literal|"wsdlversion"
decl_stmt|;
comment|// Suppress the code generation, in this case you can just get the generated code model
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SUPPRESS_GEN
init|=
literal|"suppress"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PACKAGE_NAME
init|=
literal|"defaultnamespace"
decl_stmt|;
comment|//For java2ws tool
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_LIST
init|=
literal|"serviceList"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GEN_FROM_SEI
init|=
literal|"genFromSEI"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAXWS_FRONTEND
init|=
literal|"jaxws"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIMPLE_FRONTEND
init|=
literal|"simple"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAXB_DATABINDING
init|=
literal|"jaxb"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AEGIS_DATABINDING
init|=
literal|"aegis"
decl_stmt|;
comment|//For Simple FrontEnd
specifier|public
specifier|static
specifier|final
name|String
name|SEI_CLASS
init|=
literal|"seiClass"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IMPL_CLASS
init|=
literal|"implClass"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_NAME
init|=
literal|"serviceName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORT_NAME
init|=
literal|"portName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_DATA_BINDING_NAME
init|=
literal|"jaxb"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATABIND_BEAN_NAME_SUFFIX
init|=
literal|"DatabindingBean"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_CLASS
init|=
literal|"clientClass"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_CLASS
init|=
literal|"serverClass"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_JSPREFIXMAP
init|=
literal|"javascriptPrefixMap"
decl_stmt|;
specifier|private
name|ToolConstants
parameter_list|()
block|{
comment|//utility class
block|}
block|}
end_class

end_unit

