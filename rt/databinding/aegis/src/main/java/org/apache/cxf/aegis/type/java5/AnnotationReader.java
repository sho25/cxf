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
name|aegis
operator|.
name|type
operator|.
name|java5
package|;
end_package

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
name|AnnotatedElement
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
name|Field
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlEnumValue
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
import|;
end_import

begin_class
specifier|public
class|class
name|AnnotationReader
block|{
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|WEB_PARAM
init|=
name|javax
operator|.
name|jws
operator|.
name|WebParam
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|WEB_RESULT
init|=
name|javax
operator|.
name|jws
operator|.
name|WebResult
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|XML_ATTRIBUTE
init|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|XML_ELEMENT
init|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|XML_SCHEMA
init|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSchema
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|XML_TYPE
init|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|XML_TRANSIENT
init|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
operator|.
name|class
decl_stmt|;
specifier|public
name|boolean
name|isIgnored
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
return|return
name|isAnnotationPresent
argument_list|(
name|element
argument_list|,
name|IgnoreProperty
operator|.
name|class
argument_list|,
name|XML_TRANSIENT
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isAttribute
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
return|return
name|isAnnotationPresent
argument_list|(
name|element
argument_list|,
name|XmlAttribute
operator|.
name|class
argument_list|,
name|XML_ATTRIBUTE
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isElement
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
return|return
name|isAnnotationPresent
argument_list|(
name|element
argument_list|,
name|XmlElement
operator|.
name|class
argument_list|,
name|XML_ELEMENT
argument_list|)
return|;
block|}
specifier|public
name|Boolean
name|isNillable
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|getAnnotationValue
argument_list|(
literal|"nillable"
argument_list|,
comment|// NOPMD
name|element
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|,
name|XmlElement
operator|.
name|class
argument_list|,
name|XML_ELEMENT
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Boolean
name|isNillable
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
if|if
condition|(
name|anns
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|Boolean
operator|)
name|getAnnotationValue
argument_list|(
literal|"nillable"
argument_list|,
comment|// NOPMD
name|anns
argument_list|,
name|XmlElement
operator|.
name|class
argument_list|,
name|XML_ELEMENT
argument_list|)
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|value
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|getAnnotationValue
argument_list|(
literal|"type"
argument_list|,
name|element
argument_list|,
name|AegisType
operator|.
name|class
argument_list|,
name|XmlAttribute
operator|.
name|class
argument_list|,
name|XmlElement
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// jaxb uses a different default value
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|getAnnotationValue
argument_list|(
literal|"type"
argument_list|,
name|element
argument_list|,
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
operator|.
name|DEFAULT
operator|.
name|class
argument_list|,
name|XML_ELEMENT
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getParamType
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|getAnnotationValue
argument_list|(
literal|"type"
argument_list|,
name|method
argument_list|,
name|index
argument_list|,
name|AegisType
operator|.
name|class
argument_list|,
name|XmlParamType
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getReturnType
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|getAnnotationValue
argument_list|(
literal|"type"
argument_list|,
name|element
argument_list|,
name|AegisType
operator|.
name|class
argument_list|,
name|XmlReturnType
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"name"
argument_list|,
name|element
argument_list|,
literal|""
argument_list|,
name|XmlType
operator|.
name|class
argument_list|,
name|XmlAttribute
operator|.
name|class
argument_list|,
name|XmlElement
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// jaxb uses a different default value
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"name"
argument_list|,
name|element
argument_list|,
literal|"##default"
argument_list|,
name|XML_TYPE
argument_list|,
name|XML_ATTRIBUTE
argument_list|,
name|XML_ELEMENT
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getParamTypeName
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"name"
argument_list|,
name|method
argument_list|,
name|index
argument_list|,
name|AegisType
operator|.
name|class
argument_list|,
name|XmlParamType
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|String
name|getReturnTypeName
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"name"
argument_list|,
name|element
argument_list|,
literal|""
argument_list|,
name|XmlReturnType
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
comment|// some poor class loader implementations may end not define Package elements
if|if
condition|(
name|element
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|namespace
init|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"namespace"
argument_list|,
name|element
argument_list|,
literal|""
argument_list|,
name|XmlType
operator|.
name|class
argument_list|,
name|XmlAttribute
operator|.
name|class
argument_list|,
name|XmlElement
operator|.
name|class
argument_list|,
name|XML_SCHEMA
argument_list|)
decl_stmt|;
comment|// jaxb uses a different default value
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"namespace"
argument_list|,
name|element
argument_list|,
literal|"##default"
argument_list|,
name|XML_TYPE
argument_list|,
name|XML_ATTRIBUTE
argument_list|,
name|XML_ELEMENT
argument_list|)
expr_stmt|;
block|}
return|return
name|namespace
return|;
block|}
specifier|public
name|String
name|getParamNamespace
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|String
name|namespace
init|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"namespace"
argument_list|,
name|method
argument_list|,
name|index
argument_list|,
literal|""
argument_list|,
name|XmlParamType
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// JWS annotation field is named targetNamespace
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"targetNamespace"
argument_list|,
name|method
argument_list|,
name|index
argument_list|,
literal|""
argument_list|,
name|WEB_PARAM
argument_list|)
expr_stmt|;
block|}
return|return
name|namespace
return|;
block|}
specifier|public
name|String
name|getReturnNamespace
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
name|String
name|namespace
init|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"namespace"
argument_list|,
name|element
argument_list|,
literal|""
argument_list|,
name|XmlReturnType
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// JWS annotation field is named targetNamespace
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"targetNamespace"
argument_list|,
name|element
argument_list|,
literal|""
argument_list|,
name|WEB_RESULT
argument_list|)
expr_stmt|;
block|}
return|return
name|namespace
return|;
block|}
specifier|public
name|int
name|getMinOccurs
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|)
block|{
name|String
name|minOccurs
init|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"minOccurs"
argument_list|,
name|element
argument_list|,
literal|""
argument_list|,
name|XmlElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|minOccurs
operator|!=
literal|null
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|minOccurs
argument_list|)
return|;
block|}
comment|// check jaxb annotation
name|Boolean
name|required
init|=
operator|(
name|Boolean
operator|)
name|getAnnotationValue
argument_list|(
literal|"required"
argument_list|,
name|element
argument_list|,
literal|null
argument_list|,
name|XML_ELEMENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|required
argument_list|)
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
literal|0
return|;
block|}
specifier|public
specifier|static
name|Integer
name|getMinOccurs
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
if|if
condition|(
name|anns
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|minOccurs
init|=
operator|(
name|String
operator|)
name|getAnnotationValue
argument_list|(
literal|"minOccurs"
argument_list|,
name|anns
argument_list|,
name|XmlElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|minOccurs
operator|!=
literal|null
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|minOccurs
argument_list|)
return|;
block|}
comment|// check jaxb annotation
name|Boolean
name|required
init|=
operator|(
name|Boolean
operator|)
name|getAnnotationValue
argument_list|(
literal|"required"
argument_list|,
name|anns
argument_list|,
name|XML_ELEMENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|required
argument_list|)
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isExtensibleElements
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
name|Boolean
name|extensibleElements
init|=
operator|(
name|Boolean
operator|)
name|getAnnotationValue
argument_list|(
literal|"extensibleElements"
argument_list|,
name|element
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|,
name|XmlType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|extensibleElements
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|extensibleElements
return|;
block|}
specifier|public
name|boolean
name|isExtensibleAttributes
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
name|Boolean
name|extensibleAttributes
init|=
operator|(
name|Boolean
operator|)
name|getAnnotationValue
argument_list|(
literal|"extensibleAttributes"
argument_list|,
name|element
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|,
name|XmlType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|extensibleAttributes
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|extensibleAttributes
return|;
block|}
annotation|@
name|SafeVarargs
specifier|private
specifier|static
name|boolean
name|isAnnotationPresent
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|,
comment|// NOPMD
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
modifier|...
name|annotations
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
operator|&&
name|element
operator|.
name|isAnnotationPresent
argument_list|(
name|annotation
operator|.
name|asSubclass
argument_list|(
name|Annotation
operator|.
name|class
argument_list|)
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
annotation|@
name|SafeVarargs
specifier|static
name|Object
name|getAnnotationValue
parameter_list|(
name|String
name|name
parameter_list|,
name|AnnotatedElement
name|element
parameter_list|,
name|Object
name|ignoredValue
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
modifier|...
name|annotations
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
operator|&&
name|element
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Annotation
name|ann
init|=
name|element
operator|.
name|getAnnotation
argument_list|(
name|annotation
operator|.
name|asSubclass
argument_list|(
name|Annotation
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
name|Method
name|method
init|=
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|getMethod
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|method
operator|.
name|invoke
argument_list|(
name|ann
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|ignoredValue
operator|==
literal|null
operator|&&
name|value
operator|!=
literal|null
operator|)
operator|||
operator|(
name|ignoredValue
operator|!=
literal|null
operator|&&
operator|!
name|ignoredValue
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|)
condition|)
block|{
return|return
name|value
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
comment|// annotation did not have value
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SafeVarargs
specifier|static
name|Object
name|getAnnotationValue
parameter_list|(
name|String
name|name
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
modifier|...
name|annotations
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
try|try
block|{
for|for
control|(
name|Annotation
name|ann
range|:
name|anns
control|)
block|{
if|if
condition|(
name|annotation
operator|.
name|isInstance
argument_list|(
name|ann
argument_list|)
condition|)
block|{
name|Method
name|method
init|=
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|getMethod
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|method
operator|.
name|invoke
argument_list|(
name|ann
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
comment|// annotation did not have value
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SafeVarargs
specifier|static
name|Object
name|getAnnotationValue
parameter_list|(
name|String
name|name
parameter_list|,
name|Method
name|method
parameter_list|,
name|int
name|index
parameter_list|,
name|Object
name|ignoredValue
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
modifier|...
name|annotations
parameter_list|)
block|{
if|if
condition|(
name|method
operator|.
name|getParameterAnnotations
argument_list|()
operator|==
literal|null
operator|||
name|method
operator|.
name|getParameterAnnotations
argument_list|()
operator|.
name|length
operator|<=
name|index
operator|||
name|method
operator|.
name|getParameterAnnotations
argument_list|()
index|[
name|index
index|]
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Annotation
name|ann
init|=
name|getAnnotation
argument_list|(
name|method
argument_list|,
name|index
argument_list|,
name|annotation
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|getMethod
argument_list|(
name|name
argument_list|)
operator|.
name|invoke
argument_list|(
name|ann
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|ignoredValue
operator|==
literal|null
operator|&&
name|value
operator|!=
literal|null
operator|)
operator|||
operator|(
name|ignoredValue
operator|!=
literal|null
operator|&&
operator|!
name|ignoredValue
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|)
condition|)
block|{
return|return
name|value
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
comment|// annotation did not have value
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|Annotation
name|getAnnotation
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|index
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|method
operator|.
name|getParameterAnnotations
argument_list|()
operator|==
literal|null
operator|||
name|method
operator|.
name|getParameterAnnotations
argument_list|()
operator|.
name|length
operator|<=
name|index
operator|||
name|method
operator|.
name|getParameterAnnotations
argument_list|()
index|[
name|index
index|]
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Annotation
index|[]
name|annotations
init|=
name|method
operator|.
name|getParameterAnnotations
argument_list|()
index|[
name|index
index|]
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
name|annotation
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isFlat
parameter_list|(
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
if|if
condition|(
name|annotations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Annotation
name|a
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|a
operator|instanceof
name|XmlFlattenedArray
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|String
name|getEnumValue
parameter_list|(
name|Enum
argument_list|<
name|?
argument_list|>
name|enumConstant
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Class
argument_list|<
name|?
extends|extends
name|Enum
argument_list|>
name|enumClass
init|=
name|enumConstant
operator|.
name|getClass
argument_list|()
decl_stmt|;
try|try
block|{
name|Field
name|constantField
init|=
name|enumClass
operator|.
name|getDeclaredField
argument_list|(
name|enumConstant
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|XmlEnumValue
name|constantValueAnnotation
init|=
name|constantField
operator|.
name|getAnnotation
argument_list|(
name|XmlEnumValue
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|constantValueAnnotation
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|constantValueAnnotation
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

