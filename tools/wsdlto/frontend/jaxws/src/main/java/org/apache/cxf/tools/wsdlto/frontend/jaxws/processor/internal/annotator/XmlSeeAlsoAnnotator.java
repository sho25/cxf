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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
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
name|XmlSeeAlso
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
name|JavaInterface
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
name|JavaType
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
name|util
operator|.
name|ClassCollector
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|XmlSeeAlsoAnnotator
implements|implements
name|Annotator
block|{
specifier|private
name|ClassCollector
name|collector
decl_stmt|;
specifier|public
name|XmlSeeAlsoAnnotator
parameter_list|(
name|ClassCollector
name|c
parameter_list|)
block|{
name|this
operator|.
name|collector
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|void
name|annotate
parameter_list|(
name|JavaAnnotatable
name|ja
parameter_list|)
block|{
if|if
condition|(
name|collector
operator|==
literal|null
operator|||
name|collector
operator|.
name|getTypesPackages
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|JavaInterface
name|intf
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ja
operator|instanceof
name|JavaInterface
condition|)
block|{
name|intf
operator|=
operator|(
name|JavaInterface
operator|)
name|ja
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"XmlSeeAlso can only annotate JavaInterface"
argument_list|)
throw|;
block|}
name|JAnnotation
name|jaxbAnnotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|XmlSeeAlso
operator|.
name|class
argument_list|)
decl_stmt|;
name|intf
operator|.
name|addImports
argument_list|(
name|jaxbAnnotation
operator|.
name|getImports
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JavaType
argument_list|>
name|types
init|=
operator|new
name|ArrayList
argument_list|<
name|JavaType
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|collector
operator|.
name|getTypesPackages
argument_list|()
control|)
block|{
if|if
condition|(
name|pkg
operator|.
name|equals
argument_list|(
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|)
condition|)
block|{
name|types
operator|.
name|add
argument_list|(
operator|new
name|JavaType
argument_list|(
literal|null
argument_list|,
literal|"ObjectFactory"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|types
operator|.
name|add
argument_list|(
operator|new
name|JavaType
argument_list|(
literal|null
argument_list|,
name|pkg
operator|+
literal|".ObjectFactory"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|jaxbAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|null
argument_list|,
name|types
argument_list|)
argument_list|)
expr_stmt|;
name|intf
operator|.
name|addAnnotation
argument_list|(
name|jaxbAnnotation
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

