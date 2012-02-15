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
name|bus
operator|.
name|spring
package|;
end_package

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
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|BusFactory
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
name|buslifecycle
operator|.
name|BusLifeCycleListener
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|SystemPropertyAction
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
name|configuration
operator|.
name|Configurer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_class
specifier|public
class|class
name|SpringBusFactory
extends|extends
name|BusFactory
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
name|SpringBusFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ApplicationContext
name|context
decl_stmt|;
specifier|public
name|SpringBusFactory
parameter_list|()
block|{
name|this
operator|.
name|context
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|SpringBusFactory
parameter_list|(
name|ApplicationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|Bus
name|createBus
parameter_list|()
block|{
return|return
name|createBus
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|defaultBusNotExists
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|context
condition|)
block|{
return|return
operator|!
name|context
operator|.
name|containsBean
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|Bus
name|createBus
parameter_list|(
name|String
name|cfgFile
parameter_list|)
block|{
return|return
name|createBus
argument_list|(
name|cfgFile
argument_list|,
name|defaultBusNotExists
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Bus
name|createBus
parameter_list|(
name|String
name|cfgFiles
index|[]
parameter_list|)
block|{
return|return
name|createBus
argument_list|(
name|cfgFiles
argument_list|,
name|defaultBusNotExists
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Bus
name|finishCreatingBus
parameter_list|(
name|BusApplicationContext
name|bac
parameter_list|)
block|{
specifier|final
name|Bus
name|bus
init|=
operator|(
name|Bus
operator|)
name|bac
operator|.
name|getBean
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|)
decl_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
name|bac
argument_list|,
name|BusApplicationContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|possiblySetDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|initializeBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|registerApplicationContextLifeCycleListener
argument_list|(
name|bus
argument_list|,
name|bac
argument_list|)
expr_stmt|;
if|if
condition|(
name|bus
operator|instanceof
name|SpringBus
operator|&&
name|defaultBusNotExists
argument_list|()
condition|)
block|{
operator|(
operator|(
name|SpringBus
operator|)
name|bus
operator|)
operator|.
name|setCloseContext
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|bus
return|;
block|}
specifier|public
name|Bus
name|createBus
parameter_list|(
name|String
name|cfgFile
parameter_list|,
name|boolean
name|includeDefaults
parameter_list|)
block|{
if|if
condition|(
name|cfgFile
operator|==
literal|null
condition|)
block|{
return|return
name|createBus
argument_list|(
operator|(
name|String
index|[]
operator|)
literal|null
argument_list|,
name|includeDefaults
argument_list|)
return|;
block|}
return|return
name|createBus
argument_list|(
operator|new
name|String
index|[]
block|{
name|cfgFile
block|}
argument_list|,
name|includeDefaults
argument_list|)
return|;
block|}
specifier|public
name|Bus
name|createBus
parameter_list|(
name|String
name|cfgFiles
index|[]
parameter_list|,
name|boolean
name|includeDefaults
parameter_list|)
block|{
try|try
block|{
name|String
name|userCfgFile
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_NAME
argument_list|)
decl_stmt|;
name|String
name|sysCfgFileUrl
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_URL
argument_list|)
decl_stmt|;
specifier|final
name|Resource
name|r
init|=
name|BusApplicationContext
operator|.
name|findResource
argument_list|(
name|Configurer
operator|.
name|DEFAULT_USER_CFG_FILE
argument_list|)
decl_stmt|;
name|boolean
name|exists
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|exists
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|run
parameter_list|()
block|{
return|return
name|r
operator|.
name|exists
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|==
literal|null
operator|&&
name|userCfgFile
operator|==
literal|null
operator|&&
name|cfgFiles
operator|==
literal|null
operator|&&
name|sysCfgFileUrl
operator|==
literal|null
operator|&&
operator|(
name|r
operator|==
literal|null
operator|||
operator|!
name|exists
operator|)
operator|&&
name|includeDefaults
condition|)
block|{
return|return
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|CXFBusFactory
argument_list|()
operator|.
name|createBus
argument_list|()
return|;
block|}
return|return
name|finishCreatingBus
argument_list|(
name|createApplicationContext
argument_list|(
name|cfgFiles
argument_list|,
name|includeDefaults
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BeansException
name|ex
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"APP_CONTEXT_CREATION_FAILED_MSG"
argument_list|,
name|ex
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|BusApplicationContext
name|createApplicationContext
parameter_list|(
name|String
name|cfgFiles
index|[]
parameter_list|,
name|boolean
name|includeDefaults
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|BusApplicationContext
argument_list|(
name|cfgFiles
argument_list|,
name|includeDefaults
argument_list|,
name|context
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BeansException
name|ex
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"INITIAL_APP_CONTEXT_CREATION_FAILED_MSG"
argument_list|,
name|ex
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
name|ClassLoader
name|contextLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|contextLoader
operator|!=
name|BusApplicationContext
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|BusApplicationContext
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|BusApplicationContext
argument_list|(
name|cfgFiles
argument_list|,
name|includeDefaults
argument_list|,
name|context
argument_list|)
return|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|contextLoader
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
specifier|public
name|Bus
name|createBus
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|createBus
argument_list|(
name|url
argument_list|,
name|defaultBusNotExists
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Bus
name|createBus
parameter_list|(
name|URL
index|[]
name|urls
parameter_list|)
block|{
return|return
name|createBus
argument_list|(
name|urls
argument_list|,
name|defaultBusNotExists
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Bus
name|createBus
parameter_list|(
name|URL
name|url
parameter_list|,
name|boolean
name|includeDefaults
parameter_list|)
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return
name|createBus
argument_list|(
operator|(
name|URL
index|[]
operator|)
literal|null
argument_list|,
name|includeDefaults
argument_list|)
return|;
block|}
return|return
name|createBus
argument_list|(
operator|new
name|URL
index|[]
block|{
name|url
block|}
argument_list|,
name|includeDefaults
argument_list|)
return|;
block|}
specifier|public
name|Bus
name|createBus
parameter_list|(
name|URL
index|[]
name|urls
parameter_list|,
name|boolean
name|includeDefaults
parameter_list|)
block|{
try|try
block|{
return|return
name|finishCreatingBus
argument_list|(
operator|new
name|BusApplicationContext
argument_list|(
name|urls
argument_list|,
name|includeDefaults
argument_list|,
name|context
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BeansException
name|ex
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"APP_CONTEXT_CREATION_FAILED_MSG"
argument_list|,
name|ex
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
name|void
name|registerApplicationContextLifeCycleListener
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|BusApplicationContext
name|bac
parameter_list|)
block|{
name|BusLifeCycleManager
name|lm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|lm
condition|)
block|{
name|lm
operator|.
name|registerLifeCycleListener
argument_list|(
operator|new
name|BusApplicationContextLifeCycleListener
argument_list|(
name|bac
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|BusApplicationContextLifeCycleListener
implements|implements
name|BusLifeCycleListener
block|{
specifier|private
name|BusApplicationContext
name|bac
decl_stmt|;
name|BusApplicationContextLifeCycleListener
parameter_list|(
name|BusApplicationContext
name|b
parameter_list|)
block|{
name|bac
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{         }
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{         }
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
name|bac
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

