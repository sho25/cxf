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
name|jaxrs
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Type
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
name|Set
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlAdapter
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
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
specifier|final
class|class
name|JAXBUtils
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
name|JAXBUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JAXBUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|JAXBContext
name|createJaxbContext
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
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|extraClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextProperties
parameter_list|)
block|{
if|if
condition|(
name|classes
operator|==
literal|null
operator|||
name|classes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
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
name|JAXBUtils
operator|.
name|scanPackages
argument_list|(
name|classes
argument_list|,
name|extraClass
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|JAXBContext
name|ctx
decl_stmt|;
try|try
block|{
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
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|contextProperties
argument_list|)
expr_stmt|;
return|return
name|ctx
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"No JAXB context can be created"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|void
name|closeUnmarshaller
parameter_list|(
name|Unmarshaller
name|u
parameter_list|)
block|{
if|if
condition|(
name|u
operator|instanceof
name|Closeable
condition|)
block|{
comment|//need to do this to clear the ThreadLocal cache
comment|//see https://java.net/jira/browse/JAXB-1000
try|try
block|{
operator|(
operator|(
name|Closeable
operator|)
name|u
operator|)
operator|.
name|close
argument_list|()
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
block|}
block|}
specifier|public
specifier|static
name|Object
name|convertWithAdapter
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|adapterClass
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
return|return
name|useAdapter
argument_list|(
name|obj
argument_list|,
name|getAdapter
argument_list|(
name|adapterClass
argument_list|,
name|anns
argument_list|)
argument_list|,
literal|false
argument_list|,
name|obj
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getValueTypeFromAdapter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|expectedBoundType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|defaultClass
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
try|try
block|{
name|XmlJavaTypeAdapter
name|adapter
init|=
name|getAdapter
argument_list|(
name|expectedBoundType
argument_list|,
name|anns
argument_list|)
decl_stmt|;
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|boundType
init|=
name|JAXBUtils
operator|.
name|getTypeFromAdapter
argument_list|(
name|adapter
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|boundType
operator|!=
literal|null
operator|&&
name|boundType
operator|.
name|isAssignableFrom
argument_list|(
name|expectedBoundType
argument_list|)
condition|)
block|{
return|return
name|JAXBUtils
operator|.
name|getTypeFromAdapter
argument_list|(
name|adapter
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|defaultClass
return|;
block|}
specifier|public
specifier|static
name|XmlJavaTypeAdapter
name|getAdapter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
name|XmlJavaTypeAdapter
name|typeAdapter
init|=
name|AnnotationUtils
operator|.
name|getAnnotation
argument_list|(
name|anns
argument_list|,
name|XmlJavaTypeAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeAdapter
operator|==
literal|null
condition|)
block|{
name|typeAdapter
operator|=
name|objectClass
operator|.
name|getAnnotation
argument_list|(
name|XmlJavaTypeAdapter
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|typeAdapter
operator|==
literal|null
condition|)
block|{
comment|// lets just try the 1st interface for now
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
name|objectClass
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
name|typeAdapter
operator|=
name|interfaces
operator|.
name|length
operator|>
literal|0
condition|?
name|interfaces
index|[
literal|0
index|]
operator|.
name|getAnnotation
argument_list|(
name|XmlJavaTypeAdapter
operator|.
name|class
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
block|}
return|return
name|typeAdapter
return|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getTypeFromAdapter
parameter_list|(
name|XmlJavaTypeAdapter
name|adapter
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|theType
parameter_list|,
name|boolean
name|boundType
parameter_list|)
block|{
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|adapter
operator|.
name|type
argument_list|()
operator|!=
name|XmlJavaTypeAdapter
operator|.
name|DEFAULT
operator|.
name|class
condition|)
block|{
name|theType
operator|=
name|adapter
operator|.
name|type
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Type
name|topAdapterType
init|=
name|adapter
operator|.
name|value
argument_list|()
operator|.
name|getGenericSuperclass
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|superClass
init|=
name|adapter
operator|.
name|value
argument_list|()
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
while|while
condition|(
name|superClass
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|nextSuperClass
init|=
name|superClass
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|nextSuperClass
operator|!=
literal|null
operator|&&
operator|!
name|Object
operator|.
name|class
operator|.
name|equals
argument_list|(
name|nextSuperClass
argument_list|)
condition|)
block|{
name|topAdapterType
operator|=
name|superClass
operator|.
name|getGenericSuperclass
argument_list|()
expr_stmt|;
block|}
name|superClass
operator|=
name|nextSuperClass
expr_stmt|;
block|}
name|Type
index|[]
name|types
init|=
name|InjectionUtils
operator|.
name|getActualTypes
argument_list|(
name|topAdapterType
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|!=
literal|null
operator|&&
name|types
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|int
name|index
init|=
name|boundType
condition|?
literal|1
else|:
literal|0
decl_stmt|;
name|theType
operator|=
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|types
index|[
name|index
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|theType
return|;
block|}
specifier|public
specifier|static
name|Object
name|useAdapter
parameter_list|(
name|Object
name|obj
parameter_list|,
name|XmlJavaTypeAdapter
name|typeAdapter
parameter_list|,
name|boolean
name|marshal
parameter_list|)
block|{
return|return
name|useAdapter
argument_list|(
name|obj
argument_list|,
name|typeAdapter
argument_list|,
name|marshal
argument_list|,
name|obj
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|Object
name|useAdapter
parameter_list|(
name|Object
name|obj
parameter_list|,
name|XmlJavaTypeAdapter
name|typeAdapter
parameter_list|,
name|boolean
name|marshal
parameter_list|,
name|Object
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|typeAdapter
operator|!=
literal|null
condition|)
block|{
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|XmlAdapter
name|xmlAdapter
init|=
name|typeAdapter
operator|.
name|value
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|marshal
condition|)
block|{
return|return
name|xmlAdapter
operator|.
name|marshal
argument_list|(
name|obj
argument_list|)
return|;
block|}
return|return
name|xmlAdapter
operator|.
name|unmarshal
argument_list|(
name|obj
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"(un)marshalling failed, using defaultValue"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|defaultValue
return|;
block|}
block|}
end_class

end_unit

