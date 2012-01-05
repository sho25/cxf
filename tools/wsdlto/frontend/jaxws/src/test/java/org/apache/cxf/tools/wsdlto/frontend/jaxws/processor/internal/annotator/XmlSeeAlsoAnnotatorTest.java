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
name|Iterator
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
name|util
operator|.
name|ClassCollector
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
operator|.
name|types
operator|.
name|ObjectFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|XmlSeeAlsoAnnotatorTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAddXmlSeeAlsoAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|JavaInterface
name|intf
init|=
operator|new
name|JavaInterface
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|intf
operator|.
name|getImports
argument_list|()
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|ClassCollector
name|collector
init|=
operator|new
name|ClassCollector
argument_list|()
decl_stmt|;
name|collector
operator|.
name|getTypesPackages
argument_list|()
operator|.
name|add
argument_list|(
name|ObjectFactory
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|intf
operator|.
name|annotate
argument_list|(
operator|new
name|XmlSeeAlsoAnnotator
argument_list|(
name|collector
argument_list|)
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iter
init|=
name|intf
operator|.
name|getImports
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"javax.xml.bind.annotation.XmlSeeAlso"
argument_list|,
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlSeeAlso({"
operator|+
name|ObjectFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".class})"
argument_list|,
name|intf
operator|.
name|getAnnotations
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

