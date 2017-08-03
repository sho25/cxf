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
name|extension
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
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
name|Collection
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

begin_class
specifier|public
class|class
name|Extension
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Extension
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|className
decl_stmt|;
specifier|protected
name|ClassLoader
name|classloader
decl_stmt|;
specifier|protected
specifier|volatile
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
specifier|protected
specifier|volatile
name|Class
argument_list|<
name|?
argument_list|>
name|intf
decl_stmt|;
specifier|protected
name|String
name|interfaceName
decl_stmt|;
specifier|protected
name|boolean
name|deferred
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|Object
name|args
index|[]
decl_stmt|;
specifier|protected
specifier|volatile
name|Object
name|obj
decl_stmt|;
specifier|protected
name|boolean
name|optional
decl_stmt|;
specifier|protected
name|boolean
name|notFound
decl_stmt|;
specifier|public
name|Extension
parameter_list|()
block|{     }
specifier|public
name|Extension
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|inf
parameter_list|)
block|{
name|clazz
operator|=
name|cls
expr_stmt|;
name|intf
operator|=
name|inf
expr_stmt|;
name|interfaceName
operator|=
name|inf
operator|.
name|getName
argument_list|()
expr_stmt|;
name|className
operator|=
name|cls
operator|.
name|getName
argument_list|()
expr_stmt|;
name|classloader
operator|=
name|cls
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Extension
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|clazz
operator|=
name|cls
expr_stmt|;
name|className
operator|=
name|cls
operator|.
name|getName
argument_list|()
expr_stmt|;
name|classloader
operator|=
name|cls
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Extension
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
name|classloader
operator|=
name|loader
expr_stmt|;
block|}
specifier|public
name|Extension
parameter_list|(
name|Extension
name|ext
parameter_list|)
block|{
name|className
operator|=
name|ext
operator|.
name|className
expr_stmt|;
name|interfaceName
operator|=
name|ext
operator|.
name|interfaceName
expr_stmt|;
name|deferred
operator|=
name|ext
operator|.
name|deferred
expr_stmt|;
name|namespaces
operator|=
name|ext
operator|.
name|namespaces
expr_stmt|;
name|obj
operator|=
name|ext
operator|.
name|obj
expr_stmt|;
name|clazz
operator|=
name|ext
operator|.
name|clazz
expr_stmt|;
name|intf
operator|=
name|ext
operator|.
name|intf
expr_stmt|;
name|classloader
operator|=
name|ext
operator|.
name|classloader
expr_stmt|;
name|args
operator|=
name|ext
operator|.
name|args
expr_stmt|;
name|optional
operator|=
name|ext
operator|.
name|optional
expr_stmt|;
block|}
specifier|public
name|void
name|setOptional
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|optional
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
return|return
name|optional
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|interfaceName
argument_list|)
condition|?
name|className
else|:
name|interfaceName
return|;
block|}
specifier|public
name|Object
name|getLoadedObject
parameter_list|()
block|{
return|return
name|obj
return|;
block|}
specifier|public
name|Extension
name|cloneNoObject
parameter_list|()
block|{
name|Extension
name|ext
init|=
operator|new
name|Extension
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|ext
operator|.
name|obj
operator|=
literal|null
expr_stmt|;
name|ext
operator|.
name|clazz
operator|=
literal|null
expr_stmt|;
name|ext
operator|.
name|intf
operator|=
literal|null
expr_stmt|;
return|return
name|ext
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"class: "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|", interface: "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|interfaceName
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|", deferred: "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|deferred
condition|?
literal|"true"
else|:
literal|"false"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|", namespaces: ("
argument_list|)
expr_stmt|;
name|int
name|n
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|ns
range|:
name|namespaces
control|)
block|{
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|ns
argument_list|)
expr_stmt|;
name|n
operator|++
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getClassname
parameter_list|()
block|{
return|return
name|className
return|;
block|}
specifier|public
name|void
name|setClassname
parameter_list|(
name|String
name|i
parameter_list|)
block|{
name|clazz
operator|=
literal|null
expr_stmt|;
name|notFound
operator|=
literal|false
expr_stmt|;
name|className
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|String
name|getInterfaceName
parameter_list|()
block|{
return|return
name|interfaceName
return|;
block|}
specifier|public
name|void
name|setInterfaceName
parameter_list|(
name|String
name|i
parameter_list|)
block|{
name|interfaceName
operator|=
name|i
expr_stmt|;
name|notFound
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDeferred
parameter_list|()
block|{
return|return
name|deferred
return|;
block|}
specifier|public
name|void
name|setDeferred
parameter_list|(
name|boolean
name|d
parameter_list|)
block|{
name|deferred
operator|=
name|d
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|namespaces
return|;
block|}
specifier|public
name|void
name|setArgs
parameter_list|(
name|Object
name|a
index|[]
parameter_list|)
block|{
name|args
operator|=
name|a
expr_stmt|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|tryClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|cl
parameter_list|)
block|{
name|Throwable
name|origEx
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|classloader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|classloader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|nex
parameter_list|)
block|{
comment|//ignore, fall into the stuff below
comment|//save the exception though as this is likely the important one
name|origEx
operator|=
name|nex
expr_stmt|;
block|}
block|}
try|try
block|{
return|return
name|cl
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
try|try
block|{
comment|// using the extension classloader as a fallback
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|nex
parameter_list|)
block|{
name|notFound
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|optional
condition|)
block|{
if|if
condition|(
name|origEx
operator|!=
literal|null
condition|)
block|{
name|ex
operator|=
name|origEx
expr_stmt|;
block|}
throw|throw
operator|new
name|ExtensionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"PROBLEM_LOADING_EXTENSION_CLASS"
argument_list|,
name|LOG
argument_list|,
name|name
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getClassObject
parameter_list|(
name|ClassLoader
name|cl
parameter_list|)
block|{
if|if
condition|(
name|notFound
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
return|return
name|clazz
return|;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
name|clazz
operator|=
name|tryClass
argument_list|(
name|className
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|clazz
return|;
block|}
specifier|public
name|Object
name|load
parameter_list|(
name|ClassLoader
name|cl
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
return|return
name|obj
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|getClassObject
argument_list|(
name|cl
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|notFound
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
comment|//if there is a Bus constructor, use it.
if|if
condition|(
name|b
operator|!=
literal|null
operator|&&
name|args
operator|==
literal|null
condition|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|con
init|=
name|cls
operator|.
name|getConstructor
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|=
name|con
operator|.
name|newInstance
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|obj
return|;
block|}
elseif|else
if|if
condition|(
name|b
operator|!=
literal|null
operator|&&
name|args
operator|!=
literal|null
condition|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|con
decl_stmt|;
name|boolean
name|noBus
init|=
literal|false
decl_stmt|;
try|try
block|{
name|con
operator|=
name|cls
operator|.
name|getConstructor
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|con
operator|=
name|cls
operator|.
name|getConstructor
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|noBus
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|noBus
condition|)
block|{
name|obj
operator|=
name|con
operator|.
name|newInstance
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
name|con
operator|.
name|newInstance
argument_list|(
name|b
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
elseif|else
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|con
init|=
name|cls
operator|.
name|getConstructor
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|obj
operator|=
name|con
operator|.
name|newInstance
argument_list|(
name|args
argument_list|)
expr_stmt|;
return|return
name|obj
return|;
block|}
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"PROBLEM_CREATING_EXTENSION_CLASS"
argument_list|,
name|LOG
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"PROBLEM_CREATING_EXTENSION_CLASS"
argument_list|,
name|LOG
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"PROBLEM_CREATING_EXTENSION_CLASS"
argument_list|,
name|LOG
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
name|obj
operator|=
name|cls
operator|.
name|getConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExtensionException
name|ex
parameter_list|)
block|{
name|notFound
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|optional
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Could not load optional extension "
operator|+
name|getName
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ex
parameter_list|)
block|{
name|notFound
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|optional
condition|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"PROBLEM_CREATING_EXTENSION_CLASS"
argument_list|,
name|LOG
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Could not load optional extension "
operator|+
name|getName
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
name|notFound
operator|=
literal|true
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|a
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|a
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
name|a
operator|.
name|add
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|optional
condition|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"PROBLEM_FINDING_CONSTRUCTOR"
argument_list|,
name|LOG
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|,
name|a
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Could not load optional extension "
operator|+
name|getName
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|notFound
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|optional
condition|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"PROBLEM_CREATING_EXTENSION_CLASS"
argument_list|,
name|LOG
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Could not load optional extension "
operator|+
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadInterface
parameter_list|(
name|ClassLoader
name|cl
parameter_list|)
block|{
if|if
condition|(
name|intf
operator|!=
literal|null
operator|||
name|notFound
condition|)
block|{
return|return
name|intf
return|;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|intf
operator|==
literal|null
condition|)
block|{
name|intf
operator|=
name|tryClass
argument_list|(
name|interfaceName
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|intf
return|;
block|}
block|}
end_class

end_unit

