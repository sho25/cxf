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
name|corba
operator|.
name|common
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensionRegistry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLWriter
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|SchemaFactory
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_NAME
init|=
literal|"javax.wsdl.factory.SchemaFactory"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_FILE_NAME
init|=
literal|"wsdl.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_FACTORY_IMPL_NAME
init|=
literal|"org.apache.cxf.tools.corba.processors.wsdl.SchemaFactoryImpl"
decl_stmt|;
specifier|private
specifier|static
name|String
name|fullPropertyFileName
decl_stmt|;
comment|/**      * Get a new instance of a WSDLFactory. This method      * follows (almost) the same basic sequence of steps that JAXP      * follows to determine the fully-qualified class name of the      * class which implements WSDLFactory. The steps (in order)      * are:      *<pre>      *  Check the javax.wsdl.factory.WSDLFactory system property.      *  Check the lib/wsdl.properties file in the JRE directory. The key      * will have the same name as the above system property.      *  Use the default value.      *</pre>      * Once an instance of a WSDLFactory is obtained, invoke      * newDefinition(), newWSDLReader(), or newWSDLWriter(), to create      * the desired instances.      */
specifier|public
specifier|static
name|SchemaFactory
name|newInstance
parameter_list|()
throws|throws
name|WSDLException
block|{
name|String
name|factoryImplName
init|=
name|findFactoryImplName
argument_list|()
decl_stmt|;
return|return
name|newInstance
argument_list|(
name|factoryImplName
argument_list|)
return|;
block|}
comment|/**      * Get a new instance of a WSDLFactory. This method      * returns an instance of the class factoryImplName.      * Once an instance of a WSDLFactory is obtained, invoke      * newDefinition(), newWSDLReader(), or newWSDLWriter(), to create      * the desired instances.      *      * @param factoryImplName the fully-qualified class name of the      * class which provides a concrete implementation of the abstract      * class WSDLFactory.      */
specifier|public
specifier|static
name|SchemaFactory
name|newInstance
parameter_list|(
name|String
name|factoryImplName
parameter_list|)
throws|throws
name|WSDLException
block|{
if|if
condition|(
name|factoryImplName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// get the appropriate class for the loading.
name|ClassLoader
name|loader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cl
init|=
name|loader
operator|.
name|loadClass
argument_list|(
name|factoryImplName
argument_list|)
decl_stmt|;
return|return
operator|(
name|SchemaFactory
operator|)
name|cl
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/*                  Catches:                  ClassNotFoundException                  InstantiationException                  IllegalAccessException                  */
throw|throw
operator|new
name|WSDLException
argument_list|(
name|WSDLException
operator|.
name|CONFIGURATION_ERROR
argument_list|,
literal|"Problem instantiating factory "
operator|+
literal|"implementation."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|WSDLException
argument_list|(
name|WSDLException
operator|.
name|CONFIGURATION_ERROR
argument_list|,
literal|"Unable to find name of factory "
operator|+
literal|"implementation."
argument_list|)
throw|;
block|}
comment|/**      * Create a new instance of a WSDLWriter.      */
specifier|public
specifier|abstract
name|WSDLWriter
name|newWSDLWriter
parameter_list|()
function_decl|;
comment|/**      * Create a new instance of an ExtensionRegistry with pre-registered      * serializers/deserializers for the SOAP, HTTP and MIME      * extensions. Java extensionTypes are also mapped for all      * the SOAP, HTTP and MIME extensions.      */
specifier|public
specifier|abstract
name|ExtensionRegistry
name|newPopulatedExtensionRegistry
parameter_list|()
function_decl|;
specifier|private
specifier|static
name|String
name|findFactoryImplName
parameter_list|()
block|{
name|String
name|factoryImplName
init|=
literal|null
decl_stmt|;
comment|// First, check the system property.
try|try
block|{
name|factoryImplName
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|)
expr_stmt|;
if|if
condition|(
name|factoryImplName
operator|!=
literal|null
condition|)
block|{
return|return
name|factoryImplName
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
comment|// Second, check the properties file.
name|String
name|propFileName
init|=
name|getFullPropertyFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|propFileName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|propFileName
argument_list|)
argument_list|)
init|)
block|{
name|properties
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|factoryImplName
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|)
expr_stmt|;
if|if
condition|(
name|factoryImplName
operator|!=
literal|null
condition|)
block|{
return|return
name|factoryImplName
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Third, return the default.
return|return
name|DEFAULT_FACTORY_IMPL_NAME
return|;
block|}
specifier|private
specifier|static
name|String
name|getFullPropertyFileName
parameter_list|()
block|{
if|if
condition|(
name|fullPropertyFileName
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|String
name|javaHome
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
decl_stmt|;
name|fullPropertyFileName
operator|=
name|javaHome
operator|+
name|File
operator|.
name|separator
operator|+
literal|"lib"
operator|+
name|File
operator|.
name|separator
operator|+
name|PROPERTY_FILE_NAME
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|fullPropertyFileName
return|;
block|}
block|}
end_class

end_unit

