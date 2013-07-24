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
name|transport
operator|.
name|http
operator|.
name|blueprint
package|;
end_package

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
name|Set
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|jaxb
operator|.
name|JAXBContextCache
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
name|PackageUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|HolderUtils
block|{
specifier|private
name|HolderUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|Object
name|getJaxbObject
parameter_list|(
name|Element
name|parent
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|JAXBContext
name|jaxbContext
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|jaxbClasses
parameter_list|,
name|ClassLoader
name|cl
parameter_list|)
block|{
try|try
block|{
name|Unmarshaller
name|umr
init|=
name|getContext
argument_list|(
name|c
argument_list|,
name|jaxbContext
argument_list|,
name|jaxbClasses
argument_list|,
name|cl
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|ele
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|umr
operator|.
name|unmarshal
argument_list|(
name|parent
argument_list|)
decl_stmt|;
return|return
name|ele
operator|.
name|getValue
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
specifier|synchronized
name|JAXBContext
name|getContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|JAXBContext
name|jaxbContext
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|jaxbClasses
parameter_list|,
name|ClassLoader
name|cl
parameter_list|)
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
operator|||
name|jaxbClasses
operator|==
literal|null
operator|||
operator|!
name|jaxbClasses
operator|.
name|contains
argument_list|(
name|cls
argument_list|)
condition|)
block|{
try|try
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|tmp
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|jaxbClasses
operator|!=
literal|null
condition|)
block|{
name|tmp
operator|.
name|addAll
argument_list|(
name|jaxbClasses
argument_list|)
expr_stmt|;
block|}
name|JAXBContextCache
operator|.
name|addPackage
argument_list|(
name|tmp
argument_list|,
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|cls
argument_list|)
argument_list|,
name|cls
operator|==
literal|null
condition|?
name|cl
else|:
name|cls
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|boolean
name|hasOf
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|tmp
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getPackage
argument_list|()
operator|==
name|cls
operator|.
name|getPackage
argument_list|()
operator|&&
literal|"ObjectFactory"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
name|hasOf
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|hasOf
condition|)
block|{
name|tmp
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
name|JAXBContextCache
operator|.
name|scanPackages
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|JAXBContextCache
operator|.
name|CachedContextAndSchemas
name|ccs
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|tmp
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|jaxbClasses
operator|=
name|ccs
operator|.
name|getClasses
argument_list|()
expr_stmt|;
name|jaxbContext
operator|=
name|ccs
operator|.
name|getContext
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|jaxbContext
return|;
block|}
block|}
end_class

end_unit

