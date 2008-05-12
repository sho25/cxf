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
name|endpoint
operator|.
name|dynamic
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
name|FileInputStream
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Iterator
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
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|EntityResolver
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|SAXParseException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JCodeModel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JDefinedClass
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JPackage
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|writer
operator|.
name|FileCodeWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|api
operator|.
name|ErrorListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|api
operator|.
name|S2JJAXBModel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|api
operator|.
name|SchemaCompiler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|api
operator|.
name|XJC
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
name|Bus
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
name|bus
operator|.
name|CXFBusFactory
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
name|i18n
operator|.
name|Message
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
name|logging
operator|.
name|LogUtils
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
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|ClientImpl
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
name|FileUtils
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|resource
operator|.
name|URIResolver
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|factory
operator|.
name|ServiceConstructionException
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
name|service
operator|.
name|model
operator|.
name|SchemaInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_comment
comment|/**  *   *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|DynamicClientFactory
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|DynamicClientFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|String
name|tmpdir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|simpleBindingEnabled
init|=
literal|true
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jaxbContextProperties
decl_stmt|;
specifier|private
name|DynamicClientFactory
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|public
name|void
name|setTemporaryDirectory
parameter_list|(
name|String
name|dir
parameter_list|)
block|{
name|tmpdir
operator|=
name|dir
expr_stmt|;
block|}
comment|/**      * Create a new instance using a specific<tt>Bus</tt>.      *       * @param b the<tt>Bus</tt> to use in subsequent operations with the      *            instance      * @return the new instance      */
specifier|public
specifier|static
name|DynamicClientFactory
name|newInstance
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
return|return
operator|new
name|DynamicClientFactory
argument_list|(
name|b
argument_list|)
return|;
block|}
comment|/**      * Create a new instance using a default<tt>Bus</tt>.      *       * @return the new instance      * @see CXFBusFactory#getDefaultBus()      */
specifier|public
specifier|static
name|DynamicClientFactory
name|newInstance
parameter_list|()
block|{
name|Bus
name|bus
init|=
name|CXFBusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
return|return
operator|new
name|DynamicClientFactory
argument_list|(
name|bus
argument_list|)
return|;
block|}
comment|/**      * Create a new<code>Client</code> instance using the WSDL to be loaded      * from the specified URL and using the current classloading context.      *       * @param wsdlURL the URL to load      * @return      */
specifier|public
name|Client
name|createClient
parameter_list|(
name|String
name|wsdlUrl
parameter_list|)
block|{
return|return
name|createClient
argument_list|(
name|wsdlUrl
argument_list|,
operator|(
name|QName
operator|)
literal|null
argument_list|,
operator|(
name|QName
operator|)
literal|null
argument_list|)
return|;
block|}
comment|/**      * Create a new<code>Client</code> instance using the WSDL to be loaded      * from the specified URL and with the specified<code>ClassLoader</code>      * as parent.      *       * @param wsdlUrl      * @param classLoader      * @return      */
specifier|public
name|Client
name|createClient
parameter_list|(
name|String
name|wsdlUrl
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
block|{
return|return
name|createClient
argument_list|(
name|wsdlUrl
argument_list|,
literal|null
argument_list|,
name|classLoader
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Client
name|createClient
parameter_list|(
name|String
name|wsdlUrl
parameter_list|,
name|QName
name|service
parameter_list|)
block|{
return|return
name|createClient
argument_list|(
name|wsdlUrl
argument_list|,
name|service
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Client
name|createClient
parameter_list|(
name|String
name|wsdlUrl
parameter_list|,
name|QName
name|service
parameter_list|,
name|QName
name|port
parameter_list|)
block|{
return|return
name|createClient
argument_list|(
name|wsdlUrl
argument_list|,
name|service
argument_list|,
literal|null
argument_list|,
name|port
argument_list|)
return|;
block|}
specifier|public
name|Client
name|createClient
parameter_list|(
name|String
name|wsdlUrl
parameter_list|,
name|QName
name|service
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|,
name|QName
name|port
parameter_list|)
block|{
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
name|classLoader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
block|}
name|URL
name|u
init|=
name|composeUrl
argument_list|(
name|wsdlUrl
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Creating client from URL "
operator|+
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ClientImpl
name|client
init|=
operator|new
name|ClientImpl
argument_list|(
name|bus
argument_list|,
name|u
argument_list|,
name|service
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|Service
name|svc
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
comment|//all SI's should have the same schemas
name|Collection
argument_list|<
name|SchemaInfo
argument_list|>
name|schemas
init|=
name|svc
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
name|SchemaCompiler
name|compiler
init|=
name|XJC
operator|.
name|createSchemaCompiler
argument_list|()
decl_stmt|;
name|ErrorListener
name|elForRun
init|=
operator|new
name|InnerErrorListener
argument_list|(
name|wsdlUrl
argument_list|)
decl_stmt|;
name|compiler
operator|.
name|setErrorListener
argument_list|(
name|elForRun
argument_list|)
expr_stmt|;
name|addSchemas
argument_list|(
name|wsdlUrl
argument_list|,
name|schemas
argument_list|,
name|compiler
argument_list|)
expr_stmt|;
name|S2JJAXBModel
name|intermediateModel
init|=
name|compiler
operator|.
name|bind
argument_list|()
decl_stmt|;
name|JCodeModel
name|codeModel
init|=
name|intermediateModel
operator|.
name|generateCode
argument_list|(
literal|null
argument_list|,
name|elForRun
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|firstnt
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|JPackage
argument_list|>
name|packages
init|=
name|codeModel
operator|.
name|packages
argument_list|()
init|;
name|packages
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|JPackage
name|jpackage
init|=
name|packages
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|jpackage
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"org.w3._2001.xmlschema"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
operator|!
name|jpackage
operator|.
name|classes
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|firstnt
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|firstnt
operator|=
literal|true
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|jpackage
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|outputDebug
argument_list|(
name|codeModel
argument_list|)
expr_stmt|;
name|String
name|packageList
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// our hashcode + timestamp ought to be enough.
name|String
name|stem
init|=
name|toString
argument_list|()
operator|+
literal|"-"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|File
name|src
init|=
operator|new
name|File
argument_list|(
name|tmpdir
argument_list|,
name|stem
operator|+
literal|"-src"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|src
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create working directory "
operator|+
name|src
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
try|try
block|{
name|FileCodeWriter
name|writer
init|=
operator|new
name|FileCodeWriter
argument_list|(
name|src
argument_list|)
decl_stmt|;
name|codeModel
operator|.
name|build
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to write generated Java files for schemas: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|File
name|classes
init|=
operator|new
name|File
argument_list|(
name|tmpdir
argument_list|,
name|stem
operator|+
literal|"-classes"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|classes
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create working directory "
operator|+
name|src
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
name|StringBuilder
name|classPath
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|setupClasspath
argument_list|(
name|classPath
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|File
argument_list|>
name|srcFiles
init|=
name|FileUtils
operator|.
name|getFilesRecurse
argument_list|(
name|src
argument_list|,
literal|".+\\.java$"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|compileJavaSrc
argument_list|(
name|classPath
operator|.
name|toString
argument_list|()
argument_list|,
name|srcFiles
argument_list|,
name|classes
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
operator|new
name|Message
argument_list|(
literal|"COULD_NOT_COMPILE_SRC"
argument_list|,
name|LOG
argument_list|,
name|wsdlUrl
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|FileUtils
operator|.
name|removeDir
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|URLClassLoader
name|cl
decl_stmt|;
try|try
block|{
name|cl
operator|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|classes
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
block|}
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|mue
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Internal error; a directory returns a malformed URL: "
operator|+
name|mue
operator|.
name|getMessage
argument_list|()
argument_list|,
name|mue
argument_list|)
throw|;
block|}
name|JAXBContext
name|context
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextProperties
init|=
name|jaxbContextProperties
decl_stmt|;
if|if
condition|(
name|contextProperties
operator|==
literal|null
condition|)
block|{
name|contextProperties
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|packageList
argument_list|)
condition|)
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
operator|new
name|Class
index|[
literal|0
index|]
argument_list|,
name|contextProperties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|packageList
argument_list|,
name|cl
argument_list|,
name|contextProperties
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|jbe
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create JAXBContext for generated packages: "
operator|+
name|jbe
operator|.
name|getMessage
argument_list|()
argument_list|,
name|jbe
argument_list|)
throw|;
block|}
name|JAXBDataBinding
name|databinding
init|=
operator|new
name|JAXBDataBinding
argument_list|()
decl_stmt|;
name|databinding
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|svc
operator|.
name|setDataBinding
argument_list|(
name|databinding
argument_list|)
expr_stmt|;
name|ServiceInfo
name|svcfo
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
comment|// Setup the new classloader!
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|cl
argument_list|)
expr_stmt|;
name|TypeClassInitializer
name|visitor
init|=
operator|new
name|TypeClassInitializer
argument_list|(
name|svcfo
argument_list|,
name|intermediateModel
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|walk
argument_list|()
expr_stmt|;
comment|// delete the classes files
name|FileUtils
operator|.
name|removeDir
argument_list|(
name|classes
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
specifier|private
name|void
name|outputDebug
parameter_list|(
name|JCodeModel
name|codeModel
parameter_list|)
block|{
if|if
condition|(
operator|!
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
return|return;
block|}
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|JPackage
argument_list|>
name|itr
init|=
name|codeModel
operator|.
name|packages
argument_list|()
init|;
name|itr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|JPackage
name|package1
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|JDefinedClass
argument_list|>
name|citr
init|=
name|package1
operator|.
name|classes
argument_list|()
init|;
name|citr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|citr
operator|.
name|next
argument_list|()
operator|.
name|fullName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Created classes: "
operator|+
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addSchemas
parameter_list|(
name|String
name|wsdlUrl
parameter_list|,
name|Collection
argument_list|<
name|SchemaInfo
argument_list|>
name|schemas
parameter_list|,
name|SchemaCompiler
name|compiler
parameter_list|)
block|{
name|int
name|num
init|=
literal|1
decl_stmt|;
for|for
control|(
name|SchemaInfo
name|schema
range|:
name|schemas
control|)
block|{
name|Element
name|el
init|=
name|schema
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|compiler
operator|.
name|parseSchema
argument_list|(
name|wsdlUrl
operator|+
literal|"#types"
operator|+
name|num
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|num
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|simpleBindingEnabled
operator|&&
name|isJaxb21
argument_list|()
condition|)
block|{
name|String
name|id
init|=
literal|"/org/apache/cxf/endpoint/dynamic/simple-binding.xjb"
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Loading the JAXB 2.1 simple binding for client."
argument_list|)
expr_stmt|;
name|InputSource
name|source
init|=
operator|new
name|InputSource
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|source
operator|.
name|setSystemId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|compiler
operator|.
name|parseSchema
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isJaxb21
parameter_list|()
block|{
name|String
name|id
init|=
name|Options
operator|.
name|getBuildID
argument_list|()
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|id
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|String
name|minor
init|=
literal|null
decl_stmt|;
comment|// major version
if|if
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|minor
operator|=
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|int
name|i
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|minor
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>=
literal|1
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// do nothing;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isSimpleBindingEnabled
parameter_list|()
block|{
return|return
name|simpleBindingEnabled
return|;
block|}
specifier|public
name|void
name|setSimpleBindingEnabled
parameter_list|(
name|boolean
name|simpleBindingEnabled
parameter_list|)
block|{
name|this
operator|.
name|simpleBindingEnabled
operator|=
name|simpleBindingEnabled
expr_stmt|;
block|}
specifier|static
name|boolean
name|compileJavaSrc
parameter_list|(
name|String
name|classPath
parameter_list|,
name|List
argument_list|<
name|File
argument_list|>
name|srcList
parameter_list|,
name|String
name|dest
parameter_list|)
block|{
name|String
index|[]
name|javacCommand
init|=
operator|new
name|String
index|[
name|srcList
operator|.
name|size
argument_list|()
operator|+
literal|7
index|]
decl_stmt|;
name|javacCommand
index|[
literal|0
index|]
operator|=
literal|"javac"
expr_stmt|;
name|javacCommand
index|[
literal|1
index|]
operator|=
literal|"-classpath"
expr_stmt|;
name|javacCommand
index|[
literal|2
index|]
operator|=
name|classPath
expr_stmt|;
name|javacCommand
index|[
literal|3
index|]
operator|=
literal|"-d"
expr_stmt|;
name|javacCommand
index|[
literal|4
index|]
operator|=
name|dest
expr_stmt|;
name|javacCommand
index|[
literal|5
index|]
operator|=
literal|"-target"
expr_stmt|;
name|javacCommand
index|[
literal|6
index|]
operator|=
literal|"1.5"
expr_stmt|;
name|int
name|i
init|=
literal|7
decl_stmt|;
for|for
control|(
name|File
name|f
range|:
name|srcList
control|)
block|{
name|javacCommand
index|[
name|i
operator|++
index|]
operator|=
name|f
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
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
name|Compiler
name|javaCompiler
init|=
operator|new
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
name|Compiler
argument_list|()
decl_stmt|;
return|return
name|javaCompiler
operator|.
name|internalCompile
argument_list|(
name|javacCommand
argument_list|,
literal|7
argument_list|)
return|;
block|}
specifier|static
name|void
name|addClasspathFromManifest
parameter_list|(
name|StringBuilder
name|classPath
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|IOException
block|{
name|JarFile
name|jar
init|=
operator|new
name|JarFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Attributes
name|attr
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|jar
operator|.
name|getManifest
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|attr
operator|=
name|jar
operator|.
name|getManifest
argument_list|()
operator|.
name|getMainAttributes
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|attr
operator|!=
literal|null
condition|)
block|{
name|String
name|cp
init|=
name|attr
operator|.
name|getValue
argument_list|(
literal|"Class-Path"
argument_list|)
decl_stmt|;
while|while
condition|(
name|cp
operator|!=
literal|null
condition|)
block|{
name|String
name|fileName
init|=
name|cp
decl_stmt|;
name|int
name|idx
init|=
name|fileName
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|cp
operator|=
name|cp
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cp
operator|=
literal|null
expr_stmt|;
block|}
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|File
name|f2
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|f2
operator|=
operator|new
name|File
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|f2
operator|=
operator|new
name|File
argument_list|(
name|file
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|f2
operator|.
name|exists
argument_list|()
condition|)
block|{
name|classPath
operator|.
name|append
argument_list|(
name|f2
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|classPath
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"path.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|static
name|void
name|setupClasspath
parameter_list|(
name|StringBuilder
name|classPath
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|IOException
block|{
name|ClassLoader
name|scl
init|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
decl_stmt|;
name|ClassLoader
name|tcl
init|=
name|classLoader
decl_stmt|;
do|do
block|{
if|if
condition|(
name|tcl
operator|instanceof
name|URLClassLoader
condition|)
block|{
name|URL
index|[]
name|urls
init|=
operator|(
operator|(
name|URLClassLoader
operator|)
name|tcl
operator|)
operator|.
name|getURLs
argument_list|()
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
name|urls
control|)
block|{
if|if
condition|(
name|url
operator|.
name|getProtocol
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
name|File
name|file
decl_stmt|;
try|try
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|urise
parameter_list|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|classPath
operator|.
name|append
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"path.separator"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
name|addClasspathFromManifest
argument_list|(
name|classPath
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
name|tcl
operator|=
name|tcl
operator|.
name|getParent
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|tcl
condition|)
block|{
break|break;
block|}
block|}
do|while
condition|(
operator|!
name|tcl
operator|.
name|equals
argument_list|(
name|scl
operator|.
name|getParent
argument_list|()
argument_list|)
condition|)
do|;
block|}
specifier|private
name|URL
name|composeUrl
parameter_list|(
name|String
name|s
parameter_list|)
block|{
try|try
block|{
name|URIResolver
name|resolver
init|=
operator|new
name|URIResolver
argument_list|(
literal|null
argument_list|,
name|s
argument_list|,
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|.
name|isResolved
argument_list|()
condition|)
block|{
return|return
name|resolver
operator|.
name|getURI
argument_list|()
operator|.
name|toURL
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"COULD_NOT_RESOLVE_URL"
argument_list|,
name|LOG
argument_list|,
name|s
argument_list|)
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"COULD_NOT_RESOLVE_URL"
argument_list|,
name|LOG
argument_list|,
name|s
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
class|class
name|InnerErrorListener
implements|implements
name|ErrorListener
block|{
specifier|private
name|String
name|url
decl_stmt|;
name|InnerErrorListener
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error compiling schema from WSDL at {"
operator|+
name|url
operator|+
literal|"}: "
operator|+
name|arg0
operator|.
name|getMessage
argument_list|()
argument_list|,
name|arg0
argument_list|)
throw|;
block|}
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Fatal error compiling schema from WSDL at {"
operator|+
name|url
operator|+
literal|"}: "
operator|+
name|arg0
operator|.
name|getMessage
argument_list|()
argument_list|,
name|arg0
argument_list|)
throw|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|SAXParseException
name|arg0
parameter_list|)
block|{
comment|// ignore
block|}
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|arg0
parameter_list|)
block|{
comment|// ignore
block|}
block|}
comment|// sorry, but yuck. Try a file first?!?
specifier|static
class|class
name|RelativeEntityResolver
implements|implements
name|EntityResolver
block|{
specifier|private
name|String
name|baseURI
decl_stmt|;
specifier|public
name|RelativeEntityResolver
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
block|}
specifier|public
name|InputSource
name|resolveEntity
parameter_list|(
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
block|{
comment|// the system id is null if the entity is in the wsdl.
if|if
condition|(
name|systemId
operator|!=
literal|null
condition|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|baseURI
argument_list|,
name|systemId
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
operator|new
name|InputSource
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|InputSource
argument_list|(
name|systemId
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Return the map of JAXB context properties used at the time that we create new contexts.      * @return the map      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getJaxbContextProperties
parameter_list|()
block|{
return|return
name|jaxbContextProperties
return|;
block|}
comment|/**      * Set the map of JAXB context properties used at the time that we create new contexts.      * @param jaxbContextProperties      */
specifier|public
name|void
name|setJaxbContextProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jaxbContextProperties
parameter_list|)
block|{
name|this
operator|.
name|jaxbContextProperties
operator|=
name|jaxbContextProperties
expr_stmt|;
block|}
block|}
end_class

end_unit

