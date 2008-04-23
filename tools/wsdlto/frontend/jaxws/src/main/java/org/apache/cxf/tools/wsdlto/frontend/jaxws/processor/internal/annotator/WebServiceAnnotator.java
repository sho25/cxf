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
name|jws
operator|.
name|WebService
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

begin_class
specifier|public
specifier|final
class|class
name|WebServiceAnnotator
implements|implements
name|Annotator
block|{
specifier|public
name|void
name|annotate
parameter_list|(
name|JavaAnnotatable
name|ja
parameter_list|)
block|{
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
literal|"WebService can only annotate JavaInterface"
argument_list|)
throw|;
block|}
name|JAnnotation
name|serviceAnnotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
decl_stmt|;
name|serviceAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"targetNamespace"
argument_list|,
name|intf
operator|.
name|getNamespace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|serviceAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"name"
argument_list|,
name|intf
operator|.
name|getWebServiceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|intf
operator|.
name|addAnnotation
argument_list|(
name|serviceAnnotation
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

