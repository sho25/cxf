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
name|jca
operator|.
name|core
operator|.
name|classloader
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureClassLoader
import|;
end_import

begin_comment
comment|/**  * The FireWallClassLoader is a classloader that can block request from going up  * in the classloader hierarchy.  *<P>  * Normally, when a classloader receives a request for a resource, it will  * consult its parent class loader first for that resource. The parent class  * loader is typically the System ClassLoader. If the parent class loader cannot  * provide the requested resource, the child class loader will be consulted for  * the request.<I>Note: the parent class loader must not be confused by the  * superclass of a certain class loader (e.g. SecureClassLoader). The parent  * classloader is identified at construction time and passed in as an constructor  * argument.</I>  *<P>  * Consulting the parent classloader first can be inconvenient for certain  * applications that want guarantees about which classloader is used to load a  * certain class. This could be because you want to be certain about where the  * resource came from, or you want to protect yourself against (other versions)  * of the same class that could be served by the System ClassLoader (e.g.  * because someone put them on the classpath or in the extensions directory).  *<P>  * For these cases, the FireWallClassLoader can be used.  *  *<PRE>  *  * System ClassLoader | FireWallClassLoader | User's ClassLoader  *  *</PRE>  *  * The FireWallClassLoader is placed between the user's class loader and the  * parent class loader. It has a set of filters that define what classes are  * allowed to go through. These filters describe (a groups of) packages, or a  * specific classes or resources that are allowed through to the parent  * classloader. Take as example this filter set:  *  *<pre>  * [&quot;com.iona.&quot;,&quot;javax.servlet.jsp.&quot;]  *</pre>  *  * This will allow requests to any class/resource staring with com.iona. or  * javax.servlet.jsp. through to the parent classloader and block all other  * requests.  *<P>  * A very common set of filters would be a set that allows nothing through  * except the classes used by the JDK. The JDKFireWallClassLoaderFactory  * factory class can create such FireWallClassLoader.  *<P>  * The FireWallClassLoader does not load any classes.  */
end_comment

begin_class
specifier|public
class|class
name|FireWallClassLoader
extends|extends
name|SecureClassLoader
block|{
specifier|private
specifier|final
name|String
index|[]
name|filters
decl_stmt|;
specifier|private
specifier|final
name|String
index|[]
name|fnFilters
decl_stmt|;
specifier|private
specifier|final
name|String
index|[]
name|negativeFilters
decl_stmt|;
specifier|private
specifier|final
name|String
index|[]
name|negativeFNFilters
decl_stmt|;
comment|/**      * Constructor.      *      * @param parent The Parent ClassLoader to use.      * @param fs A set of filters to let through. The filters and be either in      *            package form (<CODE>org.omg.</CODE> or<CODE>org.omg.*</CODE>)      *            or specify a single class (<CODE>junit.framework.TestCase</CODE>).      *<P>      *            When the package form is used, all classed in all subpackages      *            of this package are let trough the firewall. When the class      *            form is used, the filter only lets that single class through.      *            Note that when that class depends on another class, this class      *            does not need to be mentioned as a filter, because if the      *            originating class is loaded by the parent classloader, the      *            FireWallClassLoader will not receive requests for the      *            dependant class.      */
specifier|public
name|FireWallClassLoader
parameter_list|(
name|ClassLoader
name|parent
parameter_list|,
name|String
index|[]
name|fs
parameter_list|)
block|{
name|this
argument_list|(
name|parent
argument_list|,
name|fs
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param parent The Parent ClassLoader to use.      * @param fs A set of filters to let through. The filters and be either in      *            package form (<CODE>org.omg.</CODE> or<CODE>org.omg.*</CODE>)      *            or specify a single class (<CODE>junit.framework.TestCase</CODE>).      *<P>      *            When the package form is used, all classed in all subpackages      *            of this package are let trough the firewall. When the class      *            form is used, the filter only lets that single class through.      *            Note that when that class depends on another class, this class      *            does not need to be mentioned as a filter, because if the      *            originating class is loaded by the parent classloader, the      *            FireWallClassLoader will not receive requests for the      *            dependant class.      * @param negativeFs List of negative filters to use. Negative filters take      *            precedence over positive filters. When a class or resource is      *            requested that matches a negative filter it is not let through      *            the firewall even if an allowing filter would exist in the      *            positive filter list.      */
specifier|public
name|FireWallClassLoader
parameter_list|(
name|ClassLoader
name|parent
parameter_list|,
name|String
index|[]
name|fs
parameter_list|,
name|String
index|[]
name|negativeFs
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|filters
operator|=
name|processFilters
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|this
operator|.
name|negativeFilters
operator|=
name|processFilters
argument_list|(
name|negativeFs
argument_list|)
expr_stmt|;
name|this
operator|.
name|fnFilters
operator|=
name|filters2FNFilters
argument_list|(
name|this
operator|.
name|filters
argument_list|)
expr_stmt|;
name|this
operator|.
name|negativeFNFilters
operator|=
name|filters2FNFilters
argument_list|(
name|this
operator|.
name|negativeFilters
argument_list|)
expr_stmt|;
name|boolean
name|javaCovered
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|filters
operator|==
literal|null
condition|)
block|{
name|javaCovered
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|this
operator|.
name|filters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|this
operator|.
name|filters
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"java."
argument_list|)
condition|)
block|{
name|javaCovered
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|this
operator|.
name|negativeFilters
operator|!=
literal|null
condition|)
block|{
name|String
name|java
init|=
literal|"java."
decl_stmt|;
comment|// try all that would match java: j, ja, jav, java and java.
for|for
control|(
name|int
name|i
init|=
name|java
operator|.
name|length
argument_list|()
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|this
operator|.
name|negativeFilters
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|negativeFilters
index|[
name|j
index|]
operator|.
name|equals
argument_list|(
name|java
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|javaCovered
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|javaCovered
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"It's unsafe to construct a "
operator|+
literal|"FireWallClassLoader that does not let the java. "
operator|+
literal|"package through."
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|String
index|[]
name|processFilters
parameter_list|(
name|String
index|[]
name|fs
parameter_list|)
block|{
if|if
condition|(
name|fs
operator|==
literal|null
operator|||
name|fs
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|f
init|=
operator|new
name|String
index|[
name|fs
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|filter
init|=
name|fs
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|filter
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|filter
operator|=
name|filter
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|filter
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|f
index|[
name|i
index|]
operator|=
name|filter
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
specifier|private
specifier|static
name|String
index|[]
name|filters2FNFilters
parameter_list|(
name|String
index|[]
name|fs
parameter_list|)
block|{
if|if
condition|(
name|fs
operator|==
literal|null
operator|||
name|fs
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|f
init|=
operator|new
name|String
index|[
name|fs
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|f
index|[
name|i
index|]
operator|=
name|fs
index|[
name|i
index|]
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|resolve
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|negativeFilters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|negativeFilters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|negativeFilters
index|[
name|i
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|filters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|filters
index|[
name|i
index|]
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|loadClass
argument_list|(
name|name
argument_list|,
name|resolve
argument_list|)
return|;
block|}
block|}
block|}
else|else
block|{
return|return
name|super
operator|.
name|loadClass
argument_list|(
name|name
argument_list|,
name|resolve
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
comment|/*protected Class<?> findClass(String name) throws ClassNotFoundException {         if (negativeFilters != null) {             for (int i = 0; i< negativeFilters.length; i++) {                 if (name.startsWith(negativeFilters[i])) {                     throw new ClassNotFoundException(name);                 }             }         }          if (filters != null) {             for (int i = 0; i< filters.length; i++) {                 if (name.startsWith(filters[i])) {                     return super.findClass(name);                 }             }         } else {             return super.loadClass(name);         }         throw new ClassNotFoundException(name);     }*/
specifier|public
name|java
operator|.
name|net
operator|.
name|URL
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|negativeFNFilters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|negativeFNFilters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|negativeFNFilters
index|[
name|i
index|]
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
if|if
condition|(
name|fnFilters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fnFilters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|fnFilters
index|[
name|i
index|]
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|}
else|else
block|{
return|return
name|super
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the list of filters used by this FireWallClassLoader. The list is      * a copy of the array internally used.      *      * @return The filters used.      */
specifier|public
name|String
index|[]
name|getFilters
parameter_list|()
block|{
if|if
condition|(
name|filters
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|filters
operator|.
name|clone
argument_list|()
return|;
block|}
comment|/**      * Returns the list of negative filters used by this FireWallClassLoader.      * The list is a copy of the array internally used.      *      * @return The filters used.      */
specifier|public
name|String
index|[]
name|getNegativeFilters
parameter_list|()
block|{
if|if
condition|(
name|negativeFilters
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|negativeFilters
operator|.
name|clone
argument_list|()
return|;
block|}
block|}
end_class

end_unit

