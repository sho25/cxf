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
name|common
operator|.
name|jaxb
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
name|lang
operator|.
name|ref
operator|.
name|WeakReference
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
name|Method
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
name|PrivilegedActionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|bind
operator|.
name|annotation
operator|.
name|XmlElementDecl
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|util
operator|.
name|CacheMap
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
name|CachedClass
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JAXBContextCache
block|{
comment|/**      * Return holder of the context, classes, etc...      * Do NOT hold onto these strongly as that can lock the JAXBContext and Set<Class> objects      * into memory.  It preferred to grab the context and classes (if needed) from this object      * immediately after the call to getCachedContextAndSchemas and then discard it.  The      * main purpose of this class is to hold onto the context/set strongly until the caller      * has a chance to copy those into a place where they can hold onto it strongly as      * needed.      */
specifier|public
specifier|static
specifier|final
class|class
name|CachedContextAndSchemas
block|{
specifier|private
specifier|final
name|JAXBContext
name|context
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
decl_stmt|;
specifier|private
specifier|final
name|WeakReference
argument_list|<
name|CachedContextAndSchemasInternal
argument_list|>
name|ccas
decl_stmt|;
specifier|private
name|CachedContextAndSchemas
parameter_list|(
name|JAXBContext
name|context
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|,
name|CachedContextAndSchemasInternal
name|i
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|classes
operator|=
name|classes
expr_stmt|;
name|ccas
operator|=
operator|new
name|WeakReference
argument_list|<>
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXBContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClasses
parameter_list|()
block|{
return|return
name|classes
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DOMSource
argument_list|>
name|getSchemas
parameter_list|()
block|{
name|CachedContextAndSchemasInternal
name|i
init|=
name|ccas
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|!=
literal|null
condition|)
block|{
return|return
name|i
operator|.
name|getSchemas
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setSchemas
parameter_list|(
name|Collection
argument_list|<
name|DOMSource
argument_list|>
name|schemas
parameter_list|)
block|{
name|CachedContextAndSchemasInternal
name|i
init|=
name|ccas
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|!=
literal|null
condition|)
block|{
name|i
operator|.
name|setSchemas
argument_list|(
name|schemas
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|CachedContextAndSchemasInternal
block|{
specifier|private
specifier|final
name|WeakReference
argument_list|<
name|JAXBContext
argument_list|>
name|context
decl_stmt|;
specifier|private
specifier|final
name|WeakReference
argument_list|<
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|classes
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|DOMSource
argument_list|>
name|schemas
decl_stmt|;
name|CachedContextAndSchemasInternal
parameter_list|(
name|JAXBContext
name|context
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
operator|new
name|WeakReference
argument_list|<>
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|classes
operator|=
operator|new
name|WeakReference
argument_list|<>
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXBContext
name|getContext
parameter_list|()
block|{
return|return
name|context
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClasses
parameter_list|()
block|{
return|return
name|classes
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DOMSource
argument_list|>
name|getSchemas
parameter_list|()
block|{
return|return
name|schemas
return|;
block|}
specifier|public
name|void
name|setSchemas
parameter_list|(
name|Collection
argument_list|<
name|DOMSource
argument_list|>
name|schemas
parameter_list|)
block|{
name|this
operator|.
name|schemas
operator|=
name|schemas
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|CachedContextAndSchemasInternal
argument_list|>
argument_list|>
name|JAXBCONTEXT_CACHE
init|=
operator|new
name|CacheMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Package
argument_list|,
name|CachedClass
argument_list|>
name|OBJECT_FACTORY_CACHE
init|=
operator|new
name|CacheMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|HAS_MOXY
decl_stmt|;
static|static
block|{
name|boolean
name|b
init|=
literal|false
decl_stmt|;
try|try
block|{
name|JAXBContext
name|ctx
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|b
operator|=
name|ctx
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|".eclipse"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
name|HAS_MOXY
operator|=
name|b
expr_stmt|;
block|}
specifier|private
name|JAXBContextCache
parameter_list|()
block|{
comment|//utility class
block|}
comment|/**      * Clear any caches to make sure new contexts are created      */
specifier|public
specifier|static
name|void
name|clearCaches
parameter_list|()
block|{
synchronized|synchronized
init|(
name|JAXBCONTEXT_CACHE
init|)
block|{
name|JAXBCONTEXT_CACHE
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|OBJECT_FACTORY_CACHE
init|)
block|{
name|OBJECT_FACTORY_CACHE
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|scanPackages
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
name|JAXBUtils
operator|.
name|scanPackages
argument_list|(
name|classes
argument_list|,
name|OBJECT_FACTORY_CACHE
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|CachedContextAndSchemas
name|getCachedContextAndSchemas
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|cls
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|cls
control|)
block|{
name|classes
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|scanPackages
argument_list|(
name|classes
argument_list|)
expr_stmt|;
return|return
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|classes
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|CachedContextAndSchemas
name|getCachedContextAndSchemas
parameter_list|(
name|String
name|pkg
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|addPackage
argument_list|(
name|classes
argument_list|,
name|pkg
argument_list|,
name|loader
argument_list|)
expr_stmt|;
return|return
name|getCachedContextAndSchemas
argument_list|(
name|classes
argument_list|,
literal|null
argument_list|,
name|props
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|CachedContextAndSchemas
name|getCachedContextAndSchemas
parameter_list|(
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|,
name|String
name|defaultNs
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|,
name|Collection
argument_list|<
name|Object
argument_list|>
name|typeRefs
parameter_list|,
name|boolean
name|exact
parameter_list|)
throws|throws
name|JAXBException
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
range|:
name|classes
control|)
block|{
if|if
condition|(
name|clz
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"ObjectFactory"
argument_list|)
operator|&&
name|checkObjectFactoryNamespaces
argument_list|(
name|clz
argument_list|)
condition|)
block|{
comment|// kind of a hack, but ObjectFactories may be created with empty
comment|// namespaces
name|defaultNs
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultNs
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|HAS_MOXY
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
literal|"eclipselink.default-target-namespace"
argument_list|,
name|defaultNs
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
literal|"com.sun.xml.bind.defaultNamespaceRemap"
argument_list|,
name|defaultNs
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
name|CachedContextAndSchemasInternal
name|cachedContextAndSchemasInternal
init|=
literal|null
decl_stmt|;
name|JAXBContext
name|context
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|CachedContextAndSchemasInternal
argument_list|>
name|cachedContextAndSchemasInternalMap
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|typeRefs
operator|==
literal|null
operator|||
name|typeRefs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|JAXBCONTEXT_CACHE
init|)
block|{
if|if
condition|(
name|exact
condition|)
block|{
name|cachedContextAndSchemasInternalMap
operator|=
name|JAXBCONTEXT_CACHE
operator|.
name|get
argument_list|(
name|classes
argument_list|)
expr_stmt|;
if|if
condition|(
name|cachedContextAndSchemasInternalMap
operator|!=
literal|null
operator|&&
name|defaultNs
operator|!=
literal|null
condition|)
block|{
name|cachedContextAndSchemasInternal
operator|=
name|cachedContextAndSchemasInternalMap
operator|.
name|get
argument_list|(
name|defaultNs
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|Entry
argument_list|<
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|CachedContextAndSchemasInternal
argument_list|>
argument_list|>
name|k
range|:
name|JAXBCONTEXT_CACHE
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|key
init|=
name|k
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|key
operator|.
name|containsAll
argument_list|(
name|classes
argument_list|)
condition|)
block|{
name|cachedContextAndSchemasInternalMap
operator|=
name|k
operator|.
name|getValue
argument_list|()
expr_stmt|;
if|if
condition|(
name|defaultNs
operator|!=
literal|null
condition|)
block|{
name|cachedContextAndSchemasInternal
operator|=
name|cachedContextAndSchemasInternalMap
operator|.
name|get
argument_list|(
name|defaultNs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cachedContextAndSchemasInternal
operator|=
name|cachedContextAndSchemasInternalMap
operator|.
name|get
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
if|if
condition|(
name|cachedContextAndSchemasInternal
operator|!=
literal|null
condition|)
block|{
name|context
operator|=
name|cachedContextAndSchemasInternal
operator|.
name|getContext
argument_list|()
expr_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|cls
init|=
name|cachedContextAndSchemasInternal
operator|.
name|getClasses
argument_list|()
decl_stmt|;
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|JAXBCONTEXT_CACHE
operator|.
name|remove
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
name|cachedContextAndSchemasInternal
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
return|return
operator|new
name|CachedContextAndSchemas
argument_list|(
name|context
argument_list|,
name|cachedContextAndSchemasInternal
operator|.
name|getClasses
argument_list|()
argument_list|,
name|cachedContextAndSchemasInternal
argument_list|)
return|;
block|}
block|}
block|}
block|}
try|try
block|{
name|context
operator|=
name|createContext
argument_list|(
name|classes
argument_list|,
name|map
argument_list|,
name|typeRefs
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
comment|// load jaxb needed class and try to create jaxb context
name|boolean
name|added
init|=
name|addJaxbObjectFactory
argument_list|(
name|ex
argument_list|,
name|classes
argument_list|)
decl_stmt|;
if|if
condition|(
name|added
condition|)
block|{
try|try
block|{
name|context
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|JAXBContext
argument_list|>
argument_list|()
block|{
specifier|public
name|JAXBContext
name|run
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|classes
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
literal|0
index|]
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
name|cachedContextAndSchemasInternal
operator|=
operator|new
name|CachedContextAndSchemasInternal
argument_list|(
name|context
argument_list|,
name|classes
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|JAXBCONTEXT_CACHE
init|)
block|{
if|if
condition|(
name|typeRefs
operator|==
literal|null
operator|||
name|typeRefs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|cachedContextAndSchemasInternalMap
operator|==
literal|null
condition|)
block|{
name|cachedContextAndSchemasInternalMap
operator|=
operator|new
name|CacheMap
argument_list|<
name|String
argument_list|,
name|CachedContextAndSchemasInternal
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|cachedContextAndSchemasInternalMap
operator|.
name|put
argument_list|(
operator|(
name|defaultNs
operator|!=
literal|null
operator|)
condition|?
name|defaultNs
else|:
literal|""
argument_list|,
name|cachedContextAndSchemasInternal
argument_list|)
expr_stmt|;
name|JAXBCONTEXT_CACHE
operator|.
name|put
argument_list|(
name|classes
argument_list|,
name|cachedContextAndSchemasInternalMap
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|CachedContextAndSchemas
argument_list|(
name|context
argument_list|,
name|classes
argument_list|,
name|cachedContextAndSchemasInternal
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|boolean
name|checkObjectFactoryNamespaces
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|)
block|{
for|for
control|(
name|Method
name|meth
range|:
name|clz
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|XmlElementDecl
name|decl
init|=
name|meth
operator|.
name|getAnnotation
argument_list|(
name|XmlElementDecl
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|decl
operator|!=
literal|null
operator|&&
name|XmlElementDecl
operator|.
name|GLOBAL
operator|.
name|class
operator|.
name|equals
argument_list|(
name|decl
operator|.
name|scope
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|decl
operator|.
name|namespace
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
specifier|static
name|JAXBContext
name|createContext
parameter_list|(
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|Collection
argument_list|<
name|Object
argument_list|>
name|typeRefs
parameter_list|)
throws|throws
name|JAXBException
block|{
name|JAXBContext
name|ctx
decl_stmt|;
if|if
condition|(
name|typeRefs
operator|!=
literal|null
operator|&&
operator|!
name|typeRefs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|fact
init|=
literal|null
decl_stmt|;
name|String
name|pfx
init|=
literal|"com.sun.xml.bind."
decl_stmt|;
try|try
block|{
name|fact
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"com.sun.xml.bind.v2.ContextFactory"
argument_list|,
name|JAXBContextCache
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
try|try
block|{
name|fact
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"com.sun.xml.internal.bind.v2.ContextFactory"
argument_list|,
name|JAXBContextCache
operator|.
name|class
argument_list|)
expr_stmt|;
name|pfx
operator|=
literal|"com.sun.xml.internal.bind."
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t2
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|fact
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Method
name|m
range|:
name|fact
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
literal|"createContext"
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|9
condition|)
block|{
try|try
block|{
return|return
operator|(
name|JAXBContext
operator|)
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|classes
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|typeRefs
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"subclassReplacements"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"defaultNamespaceRemap"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"c14n"
argument_list|)
operator|==
literal|null
condition|?
name|Boolean
operator|.
name|FALSE
else|:
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"c14n"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"v2.model.annotation.RuntimeAnnotationReader"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"XmlAccessorFactory"
argument_list|)
operator|==
literal|null
condition|?
name|Boolean
operator|.
name|FALSE
else|:
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"XmlAccessorFactory"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"treatEverythingNillable"
argument_list|)
operator|==
literal|null
condition|?
name|Boolean
operator|.
name|FALSE
else|:
name|map
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"treatEverythingNillable"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"retainReferenceToInfo"
argument_list|)
operator|==
literal|null
condition|?
name|Boolean
operator|.
name|FALSE
else|:
name|map
operator|.
name|get
argument_list|(
literal|"retainReferenceToInfo"
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
block|}
block|}
try|try
block|{
name|ctx
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|JAXBContext
argument_list|>
argument_list|()
block|{
specifier|public
name|JAXBContext
name|run
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|classes
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|map
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e2
parameter_list|)
block|{
if|if
condition|(
name|e2
operator|.
name|getException
argument_list|()
operator|instanceof
name|JAXBException
condition|)
block|{
name|JAXBException
name|ex
init|=
operator|(
name|JAXBException
operator|)
name|e2
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"com.sun.xml.bind.defaultNamespaceRemap"
argument_list|)
operator|&&
name|ex
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
operator|&&
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"com.sun.xml.bind.defaultNamespaceRemap"
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
literal|"com.sun.xml.internal.bind.defaultNamespaceRemap"
argument_list|,
name|map
operator|.
name|remove
argument_list|(
literal|"com.sun.xml.bind.defaultNamespaceRemap"
argument_list|)
argument_list|)
expr_stmt|;
name|ctx
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|classes
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e2
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|ctx
return|;
block|}
comment|// Now we can not add all the classes that Jaxb needed into JaxbContext,
comment|// especially when
comment|// an ObjectFactory is pointed to by an jaxb @XmlElementDecl annotation
comment|// added this workaround method to load the jaxb needed ObjectFactory class
specifier|private
specifier|static
name|boolean
name|addJaxbObjectFactory
parameter_list|(
name|JAXBException
name|e1
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
name|boolean
name|added
init|=
literal|false
decl_stmt|;
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|java
operator|.
name|io
operator|.
name|PrintStream
name|pout
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|PrintStream
argument_list|(
name|bout
argument_list|)
decl_stmt|;
name|e1
operator|.
name|printStackTrace
argument_list|(
name|pout
argument_list|)
expr_stmt|;
name|String
name|str
init|=
operator|new
name|String
argument_list|(
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(?<=There's\\sno\\sObjectFactory\\swith\\san\\s"
operator|+
literal|"@XmlElementDecl\\sfor\\sthe\\selement\\s\\{)\\S*(?=\\})"
argument_list|)
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|str
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|pkgName
init|=
name|JAXBUtils
operator|.
name|namespaceURIToPackage
argument_list|(
name|matcher
operator|.
name|group
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|JAXBContextCache
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|pkgName
operator|+
literal|"."
operator|+
literal|"ObjectFactory"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|classes
operator|.
name|contains
argument_list|(
name|clz
argument_list|)
condition|)
block|{
name|classes
operator|.
name|add
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|added
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
block|}
return|return
name|added
return|;
block|}
specifier|public
specifier|static
name|void
name|addPackage
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|,
name|String
name|pkg
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
try|try
block|{
name|classes
operator|.
name|add
argument_list|(
name|Class
operator|.
name|forName
argument_list|(
name|pkg
operator|+
literal|".ObjectFactory"
argument_list|,
literal|false
argument_list|,
name|loader
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
try|try
init|(
name|InputStream
name|ins
init|=
name|loader
operator|.
name|getResourceAsStream
argument_list|(
literal|"/"
operator|+
name|pkg
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/jaxb.index"
argument_list|)
init|;
name|BufferedReader
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|ins
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pkg
argument_list|)
condition|)
block|{
name|pkg
operator|+=
literal|"."
expr_stmt|;
block|}
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|indexOf
argument_list|(
literal|"#"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|line
operator|.
name|indexOf
argument_list|(
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|line
argument_list|)
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|ncls
init|=
name|Class
operator|.
name|forName
argument_list|(
name|pkg
operator|+
name|line
argument_list|,
literal|false
argument_list|,
name|loader
argument_list|)
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ncls
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
end_class

end_unit

