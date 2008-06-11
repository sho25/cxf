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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|io
operator|.
name|InputStreamReader
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
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|BusFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BUS_FACTORY_PROPERTY_NAME
init|=
literal|"org.apache.cxf.bus.factory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_BUS_FACTORY
init|=
literal|"org.apache.cxf.bus.CXFBusFactory"
decl_stmt|;
specifier|protected
specifier|static
name|Bus
name|defaultBus
decl_stmt|;
specifier|protected
specifier|static
name|ThreadLocal
argument_list|<
name|Bus
argument_list|>
name|localBus
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Bus
argument_list|>
argument_list|()
decl_stmt|;
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
name|BusFactory
operator|.
name|class
argument_list|,
literal|"APIMessages"
argument_list|)
decl_stmt|;
comment|/**       * Creates a new bus.       * While concrete<code>BusFactory</code> may offer differently      * parametrized methods for creating a bus, all factories support      * this no-arg factory method.      *      * @return the newly created bus.      */
specifier|public
specifier|abstract
name|Bus
name|createBus
parameter_list|()
function_decl|;
comment|/**      * Returns the default bus, creating it if necessary.      *       * @return the default bus.      */
specifier|public
specifier|static
specifier|synchronized
name|Bus
name|getDefaultBus
parameter_list|()
block|{
return|return
name|getDefaultBus
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**      * Returns the default bus      * @param createIfNeeded Set to true to create a default bus if one doesn't exist      * @return the default bus.      */
specifier|public
specifier|static
specifier|synchronized
name|Bus
name|getDefaultBus
parameter_list|(
name|boolean
name|createIfNeeded
parameter_list|)
block|{
if|if
condition|(
name|defaultBus
operator|==
literal|null
operator|&&
name|createIfNeeded
condition|)
block|{
name|defaultBus
operator|=
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
expr_stmt|;
block|}
return|return
name|defaultBus
return|;
block|}
comment|/**      * Sets the default bus.      * @param bus the default bus.      */
specifier|public
specifier|static
specifier|synchronized
name|void
name|setDefaultBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|defaultBus
operator|=
name|bus
expr_stmt|;
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the default bus for the thread.      * @param bus the default bus.      */
specifier|public
specifier|static
name|void
name|setThreadDefaultBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|localBus
operator|.
name|set
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the default bus for the thread.      * @return the default bus.      */
specifier|public
specifier|static
name|Bus
name|getThreadDefaultBus
parameter_list|()
block|{
return|return
name|getThreadDefaultBus
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**      * Gets the default bus for the thread, creating if needed      * @param createIfNeeded Set to true to create a default bus if one doesn't exist      * @return the default bus.      */
specifier|public
specifier|static
name|Bus
name|getThreadDefaultBus
parameter_list|(
name|boolean
name|createIfNeeded
parameter_list|)
block|{
if|if
condition|(
name|createIfNeeded
operator|&&
name|localBus
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Bus
name|b
init|=
name|getDefaultBus
argument_list|(
name|createIfNeeded
argument_list|)
decl_stmt|;
name|localBus
operator|.
name|set
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
return|return
name|localBus
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * Sets the default bus if a default bus is not already set.      * @param bus the default bus.      * @return true if the bus was not set and is now set      */
specifier|public
specifier|static
specifier|synchronized
name|boolean
name|possiblySetDefaultBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|localBus
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|localBus
operator|.
name|set
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defaultBus
operator|==
literal|null
condition|)
block|{
name|defaultBus
operator|=
name|bus
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Create a new BusFactory      *       * The class of the BusFactory is determined by looking for the system propery:       * org.apache.cxf.bus.factory      * or by searching the classpath for:      * META-INF/services/org.apache.cxf.bus.factory      *       * @return a new BusFactory to be used to create Bus objects      */
specifier|public
specifier|static
name|BusFactory
name|newInstance
parameter_list|()
block|{
return|return
name|newInstance
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Create a new BusFactory      * @param className The class of the BusFactory to create.  If null, uses the      * default search algorithm.      * @return a new BusFactory to be used to create Bus objects      */
specifier|public
specifier|static
name|BusFactory
name|newInstance
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|BusFactory
name|instance
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
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
name|className
operator|=
name|getBusFactoryClass
argument_list|(
name|loader
argument_list|)
expr_stmt|;
if|if
condition|(
name|className
operator|==
literal|null
operator|&&
name|loader
operator|!=
name|BusFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
condition|)
block|{
name|className
operator|=
name|getBusFactoryClass
argument_list|(
name|BusFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
name|className
operator|=
name|BusFactory
operator|.
name|DEFAULT_BUS_FACTORY
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
extends|extends
name|BusFactory
argument_list|>
name|busFactoryClass
decl_stmt|;
try|try
block|{
name|busFactoryClass
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|className
argument_list|,
name|BusFactory
operator|.
name|class
argument_list|)
operator|.
name|asSubclass
argument_list|(
name|BusFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|instance
operator|=
name|busFactoryClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|SEVERE
argument_list|,
literal|"BUS_FACTORY_INSTANTIATION_EXC"
argument_list|,
name|ex
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
return|return
name|instance
return|;
block|}
specifier|protected
name|void
name|initializeBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|BusLifeCycleManager
name|lifeCycleManager
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
name|lifeCycleManager
condition|)
block|{
name|lifeCycleManager
operator|.
name|initComplete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getBusFactoryClass
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|String
name|busFactoryClass
init|=
literal|null
decl_stmt|;
name|String
name|busFactoryCondition
init|=
literal|null
decl_stmt|;
comment|// next check system properties
name|busFactoryClass
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|)
expr_stmt|;
if|if
condition|(
name|isValidBusFactoryClass
argument_list|(
name|busFactoryClass
argument_list|)
condition|)
block|{
return|return
name|busFactoryClass
return|;
block|}
try|try
block|{
comment|// next, check for the services stuff in the jar file
name|String
name|serviceId
init|=
literal|"META-INF/services/"
operator|+
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
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
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
name|is
operator|=
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
name|serviceId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|is
operator|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|serviceId
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
name|serviceId
operator|=
literal|"META-INF/cxf/"
operator|+
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
expr_stmt|;
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
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
name|is
operator|=
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
name|serviceId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|is
operator|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|serviceId
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|BufferedReader
name|rd
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|busFactoryClass
operator|=
name|rd
operator|.
name|readLine
argument_list|()
expr_stmt|;
name|busFactoryCondition
operator|=
name|rd
operator|.
name|readLine
argument_list|()
expr_stmt|;
name|rd
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isValidBusFactoryClass
argument_list|(
name|busFactoryClass
argument_list|)
condition|)
block|{
if|if
condition|(
name|busFactoryCondition
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|classLoader
operator|.
name|loadClass
argument_list|(
name|busFactoryCondition
argument_list|)
expr_stmt|;
return|return
name|busFactoryClass
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
return|return
name|DEFAULT_BUS_FACTORY
return|;
block|}
block|}
else|else
block|{
return|return
name|busFactoryClass
return|;
block|}
block|}
return|return
name|busFactoryClass
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|SEVERE
argument_list|,
literal|"FAILED_TO_DETERMINE_BUS_FACTORY_EXC"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
name|busFactoryClass
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isValidBusFactoryClass
parameter_list|(
name|String
name|busFactoryClassName
parameter_list|)
block|{
return|return
name|busFactoryClassName
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|busFactoryClassName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

