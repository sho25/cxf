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
name|JavaMethod
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
name|JavaParameter
import|;
end_import

begin_class
specifier|public
class|class
name|XmlJavaTypeAdapterAnnotator
implements|implements
name|Annotator
block|{
specifier|private
name|JavaInterface
name|jf
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|XmlAdapter
argument_list|>
name|adapter
decl_stmt|;
specifier|public
name|XmlJavaTypeAdapterAnnotator
parameter_list|(
name|JavaInterface
name|intf
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|XmlAdapter
argument_list|>
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|jf
operator|=
name|intf
expr_stmt|;
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
specifier|public
name|void
name|annotate
parameter_list|(
name|JavaAnnotatable
name|jn
parameter_list|)
block|{
name|JAnnotation
name|jaxbAnnotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|XmlJavaTypeAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
name|jaxbAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"value"
argument_list|,
name|adapter
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|jn
operator|instanceof
name|JavaParameter
condition|)
block|{
name|JavaParameter
name|jp
init|=
operator|(
name|JavaParameter
operator|)
name|jn
decl_stmt|;
name|jp
operator|.
name|addAnnotation
argument_list|(
literal|"XmlJavaTypeAdapter"
argument_list|,
name|jaxbAnnotation
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|jn
operator|instanceof
name|JavaMethod
condition|)
block|{
name|JavaMethod
name|jm
init|=
operator|(
name|JavaMethod
operator|)
name|jn
decl_stmt|;
name|jm
operator|.
name|addAnnotation
argument_list|(
literal|"XmlJavaTypeAdapter"
argument_list|,
name|jaxbAnnotation
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Annotation of "
operator|+
name|jn
operator|.
name|getClass
argument_list|()
operator|+
literal|" not supported."
argument_list|)
throw|;
block|}
name|jf
operator|.
name|addImport
argument_list|(
name|XmlJavaTypeAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jf
operator|.
name|addImport
argument_list|(
name|adapter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

