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
name|classloader
package|;
end_package

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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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

begin_comment
comment|/**  * This class is extremely useful for loading resources and classes in a fault  * tolerant manner that works across different applications servers. Do not  * touch this unless you're a grizzled classloading guru veteran who is going to  * verify any change on 6 different application servers.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ClassLoaderUtils
block|{
specifier|private
name|ClassLoaderUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
class|class
name|ClassLoaderHolder
block|{
name|ClassLoader
name|loader
decl_stmt|;
name|ClassLoaderHolder
parameter_list|(
name|ClassLoader
name|c
parameter_list|)
block|{
name|loader
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|ClassLoaderHolder
name|setThreadContextClassloader
parameter_list|(
specifier|final
name|ClassLoader
name|newLoader
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoaderHolder
argument_list|>
argument_list|()
block|{
specifier|public
name|ClassLoaderHolder
name|run
parameter_list|()
block|{
name|ClassLoader
name|l
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|newLoader
argument_list|)
expr_stmt|;
return|return
operator|new
name|ClassLoaderHolder
argument_list|(
name|l
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Load a given resource.<p/> This method will try to load the resource      * using the following methods (in order):      *<ul>      *<li>From Thread.currentThread().getContextClassLoader()      *<li>From ClassLoaderUtil.class.getClassLoader()      *<li>callingClass.getClassLoader()      *</ul>      *       * @param resourceName The name of the resource to load      * @param callingClass The Class object of the calling object      */
specifier|public
specifier|static
name|URL
name|getResource
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|)
block|{
name|URL
name|url
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
operator|&&
name|resourceName
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|//certain classloaders need it without the leading /
name|url
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|resourceName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ClassLoader
name|cluClassloader
init|=
name|ClassLoaderUtils
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cluClassloader
operator|==
literal|null
condition|)
block|{
name|cluClassloader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|cluClassloader
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
operator|&&
name|resourceName
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|//certain classloaders need it without the leading /
name|url
operator|=
name|cluClassloader
operator|.
name|getResource
argument_list|(
name|resourceName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|ClassLoader
name|cl
init|=
name|callingClass
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|cl
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|callingClass
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|url
operator|==
literal|null
operator|)
operator|&&
operator|(
name|resourceName
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|resourceName
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|!=
literal|'/'
operator|)
condition|)
block|{
return|return
name|getResource
argument_list|(
literal|'/'
operator|+
name|resourceName
argument_list|,
name|callingClass
argument_list|)
return|;
block|}
return|return
name|url
return|;
block|}
comment|/**      * Load a given resources.<p/> This method will try to load the resources      * using the following methods (in order):      *<ul>      *<li>From Thread.currentThread().getContextClassLoader()      *<li>From ClassLoaderUtil.class.getClassLoader()      *<li>callingClass.getClassLoader()      *</ul>      *       * @param resourceName The name of the resource to load      * @param callingClass The Class object of the calling object      */
specifier|public
specifier|static
name|List
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|)
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
operator|new
name|Enumeration
argument_list|<
name|URL
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|hasMoreElements
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|URL
name|nextElement
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|urls
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResources
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
operator|!
name|urls
operator|.
name|hasMoreElements
argument_list|()
operator|&&
name|resourceName
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|//certain classloaders need it without the leading /
try|try
block|{
name|urls
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResources
argument_list|(
name|resourceName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|ClassLoader
name|cluClassloader
init|=
name|ClassLoaderUtils
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cluClassloader
operator|==
literal|null
condition|)
block|{
name|cluClassloader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
try|try
block|{
name|urls
operator|=
name|cluClassloader
operator|.
name|getResources
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
if|if
condition|(
operator|!
name|urls
operator|.
name|hasMoreElements
argument_list|()
operator|&&
name|resourceName
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|//certain classloaders need it without the leading /
try|try
block|{
name|urls
operator|=
name|cluClassloader
operator|.
name|getResources
argument_list|(
name|resourceName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
if|if
condition|(
operator|!
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|ClassLoader
name|cl
init|=
name|callingClass
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|urls
operator|=
name|cl
operator|.
name|getResources
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
if|if
condition|(
operator|!
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|callingClass
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|urls
operator|.
name|nextElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ret
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|(
name|resourceName
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|resourceName
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|!=
literal|'/'
operator|)
condition|)
block|{
return|return
name|getResources
argument_list|(
literal|'/'
operator|+
name|resourceName
argument_list|,
name|callingClass
argument_list|)
return|;
block|}
return|return
name|ret
return|;
block|}
comment|/**      * This is a convenience method to load a resource as a stream.<p/> The      * algorithm used to find the resource is given in getResource()      *       * @param resourceName The name of the resource to load      * @param callingClass The Class object of the calling object      */
specifier|public
specifier|static
name|InputStream
name|getResourceAsStream
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|)
block|{
name|URL
name|url
init|=
name|getResource
argument_list|(
name|resourceName
argument_list|,
name|callingClass
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|(
name|url
operator|!=
literal|null
operator|)
condition|?
name|url
operator|.
name|openStream
argument_list|()
else|:
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Load a class with a given name.<p/> It will try to load the class in the      * following order:      *<ul>      *<li>From Thread.currentThread().getContextClassLoader()      *<li>Using the basic Class.forName()      *<li>From ClassLoaderUtil.class.getClassLoader()      *<li>From the callingClass.getClassLoader()      *</ul>      *       * @param className The name of the class to load      * @param callingClass The Class object of the calling object      * @throws ClassNotFoundException If the class cannot be found anywhere.      */
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|className
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
try|try
block|{
name|ClassLoader
name|cl
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
name|cl
operator|!=
literal|null
condition|)
block|{
return|return
name|cl
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|loadClass2
argument_list|(
name|className
argument_list|,
name|callingClass
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass2
parameter_list|(
name|String
name|className
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
try|try
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|ClassLoaderUtils
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|ClassLoaderUtils
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|exc
parameter_list|)
block|{
if|if
condition|(
name|callingClass
operator|!=
literal|null
operator|&&
name|callingClass
operator|.
name|getClassLoader
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|callingClass
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
return|;
block|}
block|}
throw|throw
name|ex
throw|;
block|}
block|}
block|}
end_class

end_unit

