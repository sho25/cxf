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
name|Iterator
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
name|WeakHashMap
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

begin_comment
comment|/**  * Factory to create CXF Bus objects.  *<p>CXF includes a large number of components that provide services, such  * as WSDL parsing, and message processing. To avoid creating these objects over and over, and to  * allow them to be shared easily, they are associated with a data structure called a bus.  *</p>  *<p>  * You don't ever have to explicitly create or manipulate bus objects. If you simply use the CXF  * or JAX-WS APIs to create clients or servers, CXF will create a default bus for you. You can create a bus  * explicitly if you need to customize components on the bus or maintain several independent buses  * with independent configurations.  *</p>  *<p>  * This class maintains the default bus for the entire process and a set of thread-default buses. All CXF  * components that reference the bus, which is to say all CXF components, will obtain a default bus from this  * class if you do not set a specific bus.  *</p>  *<p>  * If you create a bus when there is no default bus in effect, that bus will become the default bus.  *</p>  *<p>  * This class holds a reference to the global default bus and a reference to each thread default  * bus. The thread references are weak with respect to the threads, but otherwise ordinary.  * Thus, so long as the thread remains alive  * there will be a strong reference to the bus, and it will not get garbage-collected.  * If you want to recover memory used CXF, you can set  * the default and per-thread default bus to null, explicitly.  *</p>  */
end_comment

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
specifier|static
class|class
name|BusHolder
block|{
name|Bus
name|bus
decl_stmt|;
specifier|volatile
name|boolean
name|stale
decl_stmt|;
block|}
specifier|protected
specifier|static
specifier|final
name|Map
argument_list|<
name|Thread
argument_list|,
name|BusHolder
argument_list|>
name|THREAD_BUSSES
init|=
operator|new
name|WeakHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|BusHolder
argument_list|>
name|THREAD_BUS
init|=
operator|new
name|ThreadLocal
argument_list|<>
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
argument_list|)
decl_stmt|;
comment|/**      * Creates a new bus. While concrete<code>BusFactory</code> may offer differently parameterized methods      * for creating a bus, all factories support this no-arg factory method.      *      * @return the newly created bus.      */
specifier|public
specifier|abstract
name|Bus
name|createBus
parameter_list|()
function_decl|;
comment|/**      * Returns the default bus, creating it if necessary.      *      * @return the default bus.      */
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
comment|/**      * Returns the default bus      *      * @param createIfNeeded Set to true to create a default bus if one doesn't exist      * @return the default bus.      */
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
if|if
condition|(
name|defaultBus
operator|==
literal|null
condition|)
block|{
comment|// never set up.
return|return
literal|null
return|;
block|}
return|return
name|defaultBus
return|;
block|}
specifier|private
specifier|static
name|BusHolder
name|getThreadBusHolder
parameter_list|(
name|boolean
name|set
parameter_list|)
block|{
name|BusHolder
name|h
init|=
name|THREAD_BUS
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|h
operator|==
literal|null
operator|||
name|h
operator|.
name|stale
condition|)
block|{
name|Thread
name|cur
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|THREAD_BUSSES
init|)
block|{
name|h
operator|=
name|THREAD_BUSSES
operator|.
name|get
argument_list|(
name|cur
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|h
operator|==
literal|null
operator|||
name|h
operator|.
name|stale
condition|)
block|{
name|h
operator|=
operator|new
name|BusHolder
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|THREAD_BUSSES
init|)
block|{
name|THREAD_BUSSES
operator|.
name|put
argument_list|(
name|cur
argument_list|,
name|h
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|set
condition|)
block|{
name|THREAD_BUS
operator|.
name|set
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|h
return|;
block|}
comment|/**      * Sets the default bus.      *      * @param bus the default bus.      */
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
name|BusHolder
name|b
init|=
name|getThreadBusHolder
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|b
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|b
operator|.
name|stale
operator|=
literal|true
expr_stmt|;
name|THREAD_BUS
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Sets the default bus for the thread.      *      * @param bus the default bus.      */
specifier|public
specifier|static
name|void
name|setThreadDefaultBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|BusHolder
name|h
init|=
name|THREAD_BUS
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|h
operator|==
literal|null
condition|)
block|{
name|Thread
name|cur
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|THREAD_BUSSES
init|)
block|{
name|h
operator|=
name|THREAD_BUSSES
operator|.
name|get
argument_list|(
name|cur
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|h
operator|!=
literal|null
condition|)
block|{
name|h
operator|.
name|bus
operator|=
literal|null
expr_stmt|;
name|h
operator|.
name|stale
operator|=
literal|true
expr_stmt|;
name|THREAD_BUS
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|BusHolder
name|b
init|=
name|getThreadBusHolder
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|b
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
block|}
comment|/**      * Sets the default bus for the thread.      *      * @param bus the new thread default bus.      * @return the old thread default bus or null      */
specifier|public
specifier|static
name|Bus
name|getAndSetThreadDefaultBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|BusHolder
name|b
init|=
name|THREAD_BUS
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|Thread
name|cur
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|THREAD_BUSSES
init|)
block|{
name|b
operator|=
name|THREAD_BUSSES
operator|.
name|get
argument_list|(
name|cur
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|Bus
name|orig
init|=
name|b
operator|.
name|bus
decl_stmt|;
name|b
operator|.
name|bus
operator|=
literal|null
expr_stmt|;
name|b
operator|.
name|stale
operator|=
literal|true
expr_stmt|;
name|THREAD_BUS
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
name|orig
return|;
block|}
return|return
literal|null
return|;
block|}
name|BusHolder
name|b
init|=
name|getThreadBusHolder
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|Bus
name|old
init|=
name|b
operator|.
name|bus
decl_stmt|;
name|b
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
return|return
name|old
return|;
block|}
comment|/**      * Gets the default bus for the thread.      *      * @return the default bus.      */
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
comment|/**      * Gets the default bus for the thread, creating if needed      *      * @param createIfNeeded Set to true to create a default bus if one doesn't exist      * @return the default bus.      */
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
condition|)
block|{
name|BusHolder
name|b
init|=
name|getThreadBusHolder
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|.
name|bus
operator|==
literal|null
condition|)
block|{
name|b
operator|.
name|bus
operator|=
name|createThreadBus
argument_list|()
expr_stmt|;
block|}
return|return
name|b
operator|.
name|bus
return|;
block|}
name|BusHolder
name|h
init|=
name|THREAD_BUS
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|h
operator|==
literal|null
operator|||
name|h
operator|.
name|stale
condition|)
block|{
name|Thread
name|cur
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|THREAD_BUSSES
init|)
block|{
name|h
operator|=
name|THREAD_BUSSES
operator|.
name|get
argument_list|(
name|cur
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|h
operator|==
literal|null
operator|||
name|h
operator|.
name|stale
condition|?
literal|null
else|:
name|h
operator|.
name|bus
return|;
block|}
specifier|private
specifier|static
specifier|synchronized
name|Bus
name|createThreadBus
parameter_list|()
block|{
name|BusHolder
name|b
init|=
name|getThreadBusHolder
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|.
name|bus
operator|==
literal|null
condition|)
block|{
name|b
operator|.
name|bus
operator|=
name|getDefaultBus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|bus
return|;
block|}
comment|/**      * Removes a bus from being a thread default bus for any thread.      *<p>      * This is typically done when a bus has ended its lifecycle (i.e.: a call to      * {@link Bus#shutdown(boolean)} was invoked) and it wants to remove any reference to itself for any      * thread.      *      * @param bus the bus to remove      */
specifier|public
specifier|static
name|void
name|clearDefaultBusForAnyThread
parameter_list|(
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
synchronized|synchronized
init|(
name|THREAD_BUSSES
init|)
block|{
for|for
control|(
specifier|final
name|Iterator
argument_list|<
name|BusHolder
argument_list|>
name|iterator
init|=
name|THREAD_BUSSES
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|BusHolder
name|itBus
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|bus
operator|==
literal|null
operator|||
name|itBus
operator|==
literal|null
operator|||
name|itBus
operator|.
name|bus
operator|==
literal|null
operator|||
name|itBus
operator|.
name|stale
operator|||
name|bus
operator|.
name|equals
argument_list|(
name|itBus
operator|.
name|bus
argument_list|)
condition|)
block|{
if|if
condition|(
name|itBus
operator|!=
literal|null
condition|)
block|{
name|itBus
operator|.
name|bus
operator|=
literal|null
expr_stmt|;
comment|//mark as stale so if a thread asks again, it will create a new one
name|itBus
operator|.
name|stale
operator|=
literal|true
expr_stmt|;
block|}
comment|//This will remove the BusHolder from the only place that should
comment|//strongly reference it
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Sets the default bus if a default bus is not already set.      *      * @param bus the default bus.      * @return true if the bus was not set and is now set      */
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
name|BusHolder
name|b
init|=
name|getThreadBusHolder
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|.
name|bus
operator|==
literal|null
condition|)
block|{
name|b
operator|.
name|bus
operator|=
name|bus
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
comment|/**      * Create a new BusFactory The class of the BusFactory is determined by looking for the system propery:      * org.apache.cxf.bus.factory or by searching the classpath for:      * META-INF/services/org.apache.cxf.bus.factory      *      * @return a new BusFactory to be used to create Bus objects      */
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
comment|/**      * Create a new BusFactory      *      * @param className The class of the BusFactory to create. If null, uses the default search algorithm.      * @return a new BusFactory to be used to create Bus objects      */
specifier|public
specifier|static
name|BusFactory
name|newInstance
parameter_list|(
name|String
name|className
parameter_list|)
block|{
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
try|try
block|{
name|Class
argument_list|<
name|?
extends|extends
name|BusFactory
argument_list|>
name|busFactoryClass
init|=
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
decl_stmt|;
return|return
name|busFactoryClass
operator|.
name|getConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
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
block|}
specifier|protected
name|void
name|initializeBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{     }
specifier|private
specifier|static
name|String
name|getBusFactoryClass
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
comment|// next check system properties
name|String
name|busFactoryClass
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|)
decl_stmt|;
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
name|String
name|busFactoryCondition
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
init|(
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
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
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
block|}
block|}
if|if
condition|(
name|isValidBusFactoryClass
argument_list|(
name|busFactoryClass
argument_list|)
operator|&&
name|busFactoryCondition
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|busFactoryClass
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
decl_stmt|;
if|if
condition|(
name|busFactoryCondition
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|busFactoryCondition
operator|=
name|busFactoryCondition
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|idx
init|=
name|busFactoryCondition
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
decl_stmt|;
while|while
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|cls
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|busFactoryCondition
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
name|busFactoryCondition
operator|=
name|busFactoryCondition
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
name|idx
operator|=
name|busFactoryCondition
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|cls
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|busFactoryCondition
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
decl||
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
name|busFactoryClass
operator|=
name|DEFAULT_BUS_FACTORY
expr_stmt|;
block|}
block|}
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
name|busFactoryClassName
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

