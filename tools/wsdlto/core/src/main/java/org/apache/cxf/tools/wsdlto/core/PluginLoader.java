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
name|wsdlto
operator|.
name|core
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

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
name|FileNotFoundException
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
name|net
operator|.
name|URL
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
name|LinkedHashMap
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
name|TreeMap
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
name|JAXBElement
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
name|bind
operator|.
name|Unmarshaller
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|tools
operator|.
name|common
operator|.
name|FrontEndGenerator
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
name|Processor
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
name|ToolContainer
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
name|plugin
operator|.
name|DataBinding
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
name|plugin
operator|.
name|FrontEnd
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
name|plugin
operator|.
name|Generator
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
name|plugin
operator|.
name|Plugin
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|PluginLoader
block|{
specifier|public
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|PluginLoader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PROVIDER_NAME
init|=
literal|"cxf.apache.org"
decl_stmt|;
specifier|private
specifier|static
name|PluginLoader
name|pluginLoader
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PLUGIN_FILE_NAME
init|=
literal|"META-INF/tools-plugin.xml"
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Plugin
argument_list|>
name|plugins
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Plugin
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|FrontEnd
argument_list|>
name|frontends
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|FrontEnd
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DataBinding
argument_list|>
name|databindings
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|DataBinding
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Unmarshaller
name|unmarshaller
decl_stmt|;
specifier|private
name|PluginLoader
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
try|try
block|{
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.cxf.tools.plugin"
argument_list|)
decl_stmt|;
name|unmarshaller
operator|=
name|jc
operator|.
name|createUnmarshaller
argument_list|()
expr_stmt|;
name|loadPlugins
argument_list|(
name|ClassLoaderUtils
operator|.
name|getResources
argument_list|(
name|PLUGIN_FILE_NAME
argument_list|,
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"JAXB_CONTEXT_INIT_FAIL"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"LOAD_PLUGIN_EXCEPTION"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|refresh
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|loadPlugins
parameter_list|(
name|List
argument_list|<
name|URL
argument_list|>
name|pluginFiles
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|pluginFiles
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"FOUND_NO_PLUGINS"
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|URL
name|url
range|:
name|pluginFiles
control|)
block|{
name|loadPlugin
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|PluginLoader
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|PluginLoader
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|PluginLoader
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|pluginLoader
operator|==
literal|null
condition|)
block|{
name|pluginLoader
operator|=
operator|new
name|PluginLoader
argument_list|()
expr_stmt|;
block|}
return|return
name|pluginLoader
return|;
block|}
specifier|public
specifier|static
name|void
name|unload
parameter_list|()
block|{
name|pluginLoader
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|loadPlugin
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"PLUGIN_LOADING"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|loadPlugin
argument_list|(
name|getPlugin
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PLUGIN_LOAD_FAIL"
argument_list|,
name|LOG
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|loadPlugin
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"PLUGIN_LOADING"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|loadPlugin
argument_list|(
name|getPlugin
argument_list|(
name|resource
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PLUGIN_LOAD_FAIL"
argument_list|,
name|LOG
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|fe
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PLUGIN_FILE_NOT_FOUND"
argument_list|,
name|LOG
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|fe
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|loadPlugin
parameter_list|(
name|Plugin
name|plugin
parameter_list|)
block|{
if|if
condition|(
name|plugin
operator|.
name|getFrontend
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"FOUND_FRONTENDS"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|plugin
operator|.
name|getName
argument_list|()
block|,
name|plugin
operator|.
name|getFrontend
argument_list|()
operator|.
name|size
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|FrontEnd
name|frontend
range|:
name|plugin
operator|.
name|getFrontend
argument_list|()
control|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"LOADING_FRONTEND"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|frontend
operator|.
name|getName
argument_list|()
block|,
name|plugin
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|frontend
operator|.
name|getName
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
name|WARNING
argument_list|,
literal|"FRONTEND_MISSING_NAME"
argument_list|,
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|frontends
operator|.
name|containsKey
argument_list|(
name|frontend
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|DEFAULT_PROVIDER_NAME
operator|.
name|equals
argument_list|(
name|plugin
operator|.
name|getProvider
argument_list|()
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"REPLACED_DEFAULT_FRONTEND"
argument_list|,
name|LOG
argument_list|,
name|frontend
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|frontends
operator|.
name|put
argument_list|(
name|frontend
operator|.
name|getName
argument_list|()
argument_list|,
name|frontend
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|plugin
operator|.
name|getDatabinding
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"FOUND_DATABINDINGS"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|plugin
operator|.
name|getName
argument_list|()
block|,
name|plugin
operator|.
name|getDatabinding
argument_list|()
operator|.
name|size
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DataBinding
name|databinding
range|:
name|plugin
operator|.
name|getDatabinding
argument_list|()
control|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"LOADING_DATABINDING"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|databinding
operator|.
name|getName
argument_list|()
block|,
name|plugin
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|databinding
operator|.
name|getName
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
name|WARNING
argument_list|,
literal|"DATABINDING_MISSING_NAME"
argument_list|,
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|databindings
operator|.
name|containsKey
argument_list|(
name|databinding
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|DEFAULT_PROVIDER_NAME
operator|.
name|equals
argument_list|(
name|plugin
operator|.
name|getProvider
argument_list|()
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"REPLACED_DEFAULT_DATABINDING"
argument_list|,
name|LOG
argument_list|,
name|databinding
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|databindings
operator|.
name|put
argument_list|(
name|databinding
operator|.
name|getName
argument_list|()
argument_list|,
name|databinding
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Plugin
name|getPlugin
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
throws|,
name|JAXBException
throws|,
name|FileNotFoundException
block|{
name|Plugin
name|plugin
init|=
name|plugins
operator|.
name|get
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|plugin
operator|==
literal|null
condition|)
block|{
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|plugin
operator|=
name|getPlugin
argument_list|(
name|is
argument_list|)
expr_stmt|;
if|if
condition|(
name|plugin
operator|==
literal|null
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PLUGIN_LOAD_FAIL"
argument_list|,
name|LOG
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|plugins
operator|.
name|put
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|,
name|plugin
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
name|getPlugin
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
name|plugin
return|;
block|}
specifier|protected
name|Plugin
name|getPlugin
parameter_list|(
name|String
name|resource
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|FileNotFoundException
block|{
name|Plugin
name|plugin
init|=
name|plugins
operator|.
name|get
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|plugin
operator|==
literal|null
condition|)
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|new
name|File
argument_list|(
name|resource
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
name|is
operator|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|resource
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PLUGIN_MISSING"
argument_list|,
name|LOG
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|plugin
operator|=
name|getPlugin
argument_list|(
name|is
argument_list|)
expr_stmt|;
if|if
condition|(
name|plugin
operator|==
literal|null
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PLUGIN_LOAD_FAIL"
argument_list|,
name|LOG
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|plugins
operator|.
name|put
argument_list|(
name|resource
argument_list|,
name|plugin
argument_list|)
expr_stmt|;
block|}
return|return
name|plugin
return|;
block|}
specifier|private
name|Plugin
name|getPlugin
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|JAXBException
block|{
return|return
call|(
name|Plugin
call|)
argument_list|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
argument_list|)
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|FrontEnd
name|getFrontEnd
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|FrontEnd
name|frontend
init|=
name|frontends
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|frontend
operator|==
literal|null
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FRONTEND_MISSING"
argument_list|,
name|LOG
argument_list|,
name|name
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
return|return
name|frontend
return|;
block|}
specifier|private
name|String
name|getGeneratorClass
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|,
name|Generator
name|generator
parameter_list|)
block|{
name|String
name|fullPackage
init|=
name|generator
operator|.
name|getPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|fullPackage
argument_list|)
condition|)
block|{
name|fullPackage
operator|=
name|frontend
operator|.
name|getGenerators
argument_list|()
operator|.
name|getPackage
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|fullPackage
argument_list|)
condition|)
block|{
name|fullPackage
operator|=
name|frontend
operator|.
name|getPackage
argument_list|()
expr_stmt|;
block|}
return|return
name|fullPackage
operator|+
literal|"."
operator|+
name|generator
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
name|List
argument_list|<
name|FrontEndGenerator
argument_list|>
name|getFrontEndGenerators
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|)
block|{
name|List
argument_list|<
name|FrontEndGenerator
argument_list|>
name|generators
init|=
operator|new
name|ArrayList
argument_list|<
name|FrontEndGenerator
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|fullClzName
init|=
literal|null
decl_stmt|;
try|try
block|{
for|for
control|(
name|Generator
name|generator
range|:
name|frontend
operator|.
name|getGenerators
argument_list|()
operator|.
name|getGenerator
argument_list|()
control|)
block|{
name|fullClzName
operator|=
name|getGeneratorClass
argument_list|(
name|frontend
argument_list|,
name|generator
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|fullClzName
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|generators
operator|.
name|add
argument_list|(
operator|(
name|FrontEndGenerator
operator|)
name|clz
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FRONTEND_PROFILE_LOAD_FAIL"
argument_list|,
name|LOG
argument_list|,
name|fullClzName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|generators
return|;
block|}
specifier|private
name|FrontEndProfile
name|loadFrontEndProfile
parameter_list|(
name|String
name|fullClzName
parameter_list|)
block|{
name|FrontEndProfile
name|profile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|fullClzName
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|profile
operator|=
operator|(
name|FrontEndProfile
operator|)
name|clz
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FRONTEND_PROFILE_LOAD_FAIL"
argument_list|,
name|LOG
argument_list|,
name|fullClzName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|profile
return|;
block|}
specifier|private
name|Processor
name|loadProcessor
parameter_list|(
name|String
name|fullClzName
parameter_list|)
block|{
name|Processor
name|processor
init|=
literal|null
decl_stmt|;
try|try
block|{
name|processor
operator|=
operator|(
name|Processor
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|fullClzName
argument_list|,
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"LOAD_PROCESSOR_FAILED"
argument_list|,
name|LOG
argument_list|,
name|fullClzName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|processor
return|;
block|}
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|ToolContainer
argument_list|>
name|loadContainerClass
parameter_list|(
name|String
name|fullClzName
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
literal|null
decl_stmt|;
try|try
block|{
name|clz
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|fullClzName
argument_list|,
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"LOAD_CONTAINER_CLASS_FAILED"
argument_list|,
name|LOG
argument_list|,
name|fullClzName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|ToolContainer
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clz
argument_list|)
condition|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"CLZ_SHOULD_IMPLEMENT_INTERFACE"
argument_list|,
name|LOG
argument_list|,
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|message
argument_list|)
throw|;
block|}
return|return
name|clz
operator|.
name|asSubclass
argument_list|(
name|ToolContainer
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
name|String
name|getFrontEndProfileClass
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|frontend
operator|.
name|getProfile
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"org.apache.cxf.tools.wsdlto.core.FrontEndProfile"
return|;
block|}
return|return
name|frontend
operator|.
name|getPackage
argument_list|()
operator|+
literal|"."
operator|+
name|frontend
operator|.
name|getProfile
argument_list|()
return|;
block|}
specifier|private
name|String
name|getProcessorClass
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|)
block|{
name|String
name|pkgName
init|=
name|frontend
operator|.
name|getProcessor
argument_list|()
operator|.
name|getPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pkgName
argument_list|)
condition|)
block|{
name|pkgName
operator|=
name|frontend
operator|.
name|getPackage
argument_list|()
expr_stmt|;
block|}
return|return
name|pkgName
operator|+
literal|"."
operator|+
name|frontend
operator|.
name|getProcessor
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
name|String
name|getContainerClass
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|)
block|{
return|return
name|getContainerPackage
argument_list|(
name|frontend
argument_list|)
operator|+
literal|"."
operator|+
name|frontend
operator|.
name|getContainer
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
name|String
name|getContainerPackage
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|)
block|{
name|String
name|pkgName
init|=
name|frontend
operator|.
name|getContainer
argument_list|()
operator|.
name|getPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pkgName
argument_list|)
condition|)
block|{
name|pkgName
operator|=
name|frontend
operator|.
name|getPackage
argument_list|()
expr_stmt|;
block|}
return|return
name|pkgName
return|;
block|}
specifier|private
name|String
name|getToolspec
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|)
block|{
name|String
name|toolspec
init|=
name|frontend
operator|.
name|getContainer
argument_list|()
operator|.
name|getToolspec
argument_list|()
decl_stmt|;
return|return
literal|"/"
operator|+
name|getContainerPackage
argument_list|(
name|frontend
argument_list|)
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|"/"
operator|+
name|toolspec
return|;
block|}
specifier|private
name|AbstractWSDLBuilder
name|loadBuilder
parameter_list|(
name|String
name|fullClzName
parameter_list|)
block|{
name|AbstractWSDLBuilder
name|builder
init|=
literal|null
decl_stmt|;
try|try
block|{
name|builder
operator|=
operator|(
name|AbstractWSDLBuilder
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|fullClzName
argument_list|,
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"LOAD_PROCESSOR_FAILED"
argument_list|,
name|LOG
argument_list|,
name|fullClzName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|builder
return|;
block|}
specifier|private
name|String
name|getBuilderClass
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|)
block|{
name|String
name|pkgName
init|=
name|frontend
operator|.
name|getBuilder
argument_list|()
operator|.
name|getPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pkgName
argument_list|)
condition|)
block|{
name|pkgName
operator|=
name|frontend
operator|.
name|getPackage
argument_list|()
expr_stmt|;
block|}
return|return
name|pkgName
operator|+
literal|"."
operator|+
name|frontend
operator|.
name|getBuilder
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|FrontEndProfile
name|getFrontEndProfile
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|FrontEndProfile
name|profile
init|=
literal|null
decl_stmt|;
name|FrontEnd
name|frontend
init|=
name|getFrontEnd
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|profile
operator|=
name|loadFrontEndProfile
argument_list|(
name|getFrontEndProfileClass
argument_list|(
name|frontend
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|FrontEndGenerator
name|generator
range|:
name|getFrontEndGenerators
argument_list|(
name|frontend
argument_list|)
control|)
block|{
name|profile
operator|.
name|registerGenerator
argument_list|(
name|generator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|frontend
operator|.
name|getProcessor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|profile
operator|.
name|setProcessor
argument_list|(
name|loadProcessor
argument_list|(
name|getProcessorClass
argument_list|(
name|frontend
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|frontend
operator|.
name|getContainer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|profile
operator|.
name|setContainerClass
argument_list|(
name|loadContainerClass
argument_list|(
name|getContainerClass
argument_list|(
name|frontend
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setToolspec
argument_list|(
name|getToolspec
argument_list|(
name|frontend
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|frontend
operator|.
name|getBuilder
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|profile
operator|.
name|setWSDLBuilder
argument_list|(
name|loadBuilder
argument_list|(
name|getBuilderClass
argument_list|(
name|frontend
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|profile
return|;
block|}
specifier|public
name|DataBinding
name|getDataBinding
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|DataBinding
name|databinding
init|=
name|databindings
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|databinding
operator|==
literal|null
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"DATABINDING_MISSING"
argument_list|,
name|LOG
argument_list|,
name|name
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
return|return
name|databinding
return|;
block|}
specifier|private
name|DataBindingProfile
name|loadDataBindingProfile
parameter_list|(
name|String
name|fullClzName
parameter_list|)
block|{
name|DataBindingProfile
name|profile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|profile
operator|=
operator|(
name|DataBindingProfile
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|fullClzName
argument_list|,
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"DATABINDING_PROFILE_LOAD_FAIL"
argument_list|,
name|LOG
argument_list|,
name|fullClzName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
return|return
name|profile
return|;
block|}
specifier|public
name|DataBindingProfile
name|getDataBindingProfile
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|DataBindingProfile
name|profile
init|=
literal|null
decl_stmt|;
name|DataBinding
name|databinding
init|=
name|getDataBinding
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|profile
operator|=
name|loadDataBindingProfile
argument_list|(
name|databinding
operator|.
name|getPackage
argument_list|()
operator|+
literal|"."
operator|+
name|databinding
operator|.
name|getProfile
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|profile
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|FrontEnd
argument_list|>
name|getFrontEnds
parameter_list|()
block|{
return|return
name|this
operator|.
name|frontends
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DataBinding
argument_list|>
name|getDataBindings
parameter_list|()
block|{
return|return
name|this
operator|.
name|databindings
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Plugin
argument_list|>
name|getPlugins
parameter_list|()
block|{
return|return
name|this
operator|.
name|plugins
return|;
block|}
block|}
end_class

end_unit

