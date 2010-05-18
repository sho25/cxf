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
name|tools
operator|.
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|annotator
package|;
end_package

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
name|List
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
name|XmlAccessType
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
name|XmlAccessorType
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
name|XmlRootElement
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
name|XmlType
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|Annotator
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JAnnotation
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JAnnotationElement
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaAnnotatable
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaField
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
name|tools
operator|.
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|model
operator|.
name|WrapperBeanClass
import|;
end_import

begin_class
specifier|public
class|class
name|WrapperBeanAnnotator
implements|implements
name|Annotator
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|sourceClass
decl_stmt|;
specifier|public
name|WrapperBeanAnnotator
parameter_list|()
block|{              }
specifier|public
name|WrapperBeanAnnotator
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|this
operator|.
name|sourceClass
operator|=
name|cls
expr_stmt|;
block|}
specifier|public
name|void
name|annotate
parameter_list|(
specifier|final
name|JavaAnnotatable
name|clz
parameter_list|)
block|{
name|WrapperBeanClass
name|beanClass
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|clz
operator|instanceof
name|WrapperBeanClass
condition|)
block|{
name|beanClass
operator|=
operator|(
name|WrapperBeanClass
operator|)
name|clz
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"WrapperBeanAnnotator expect JavaClass as input"
argument_list|)
throw|;
block|}
name|JAnnotation
name|xmlRootElement
init|=
operator|new
name|JAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|xmlRootElement
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"name"
argument_list|,
name|beanClass
operator|.
name|getElementName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|xmlRootElement
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"namespace"
argument_list|,
name|beanClass
operator|.
name|getElementName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|JAnnotation
name|xmlAccessorType
init|=
operator|new
name|JAnnotation
argument_list|(
name|XmlAccessorType
operator|.
name|class
argument_list|)
decl_stmt|;
name|xmlAccessorType
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|null
argument_list|,
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|XmlType
name|tp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|sourceClass
operator|!=
literal|null
condition|)
block|{
name|tp
operator|=
name|sourceClass
operator|.
name|getAnnotation
argument_list|(
name|XmlType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|JAnnotation
name|xmlType
init|=
operator|new
name|JAnnotation
argument_list|(
name|XmlType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tp
operator|==
literal|null
condition|)
block|{
name|xmlType
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"name"
argument_list|,
name|beanClass
operator|.
name|getElementName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|xmlType
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"namespace"
argument_list|,
name|beanClass
operator|.
name|getElementName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
literal|"##default"
operator|.
name|equals
argument_list|(
name|tp
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|xmlType
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"name"
argument_list|,
name|tp
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|"##default"
operator|.
name|equals
argument_list|(
name|tp
operator|.
name|namespace
argument_list|()
argument_list|)
condition|)
block|{
name|xmlType
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"namespace"
argument_list|,
name|tp
operator|.
name|namespace
argument_list|()
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
name|tp
operator|.
name|factoryMethod
argument_list|()
argument_list|)
condition|)
block|{
name|xmlType
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"factoryMethod"
argument_list|,
name|tp
operator|.
name|factoryMethod
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tp
operator|.
name|propOrder
argument_list|()
operator|.
name|length
operator|!=
literal|1
operator|||
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|tp
operator|.
name|propOrder
argument_list|()
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|xmlType
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"propOrder"
argument_list|,
name|tp
operator|.
name|propOrder
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|String
argument_list|>
name|props
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JavaField
name|f
range|:
name|beanClass
operator|.
name|getFields
argument_list|()
control|)
block|{
name|props
operator|.
name|add
argument_list|(
name|f
operator|.
name|getParaName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|props
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|xmlType
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"propOrder"
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Revisit: why annotation is string?
name|beanClass
operator|.
name|addAnnotation
argument_list|(
name|xmlRootElement
argument_list|)
expr_stmt|;
name|beanClass
operator|.
name|addAnnotation
argument_list|(
name|xmlAccessorType
argument_list|)
expr_stmt|;
name|beanClass
operator|.
name|addAnnotation
argument_list|(
name|xmlType
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

